package org.lummerland.bitbucket.hook;

import java.util.Arrays;
import java.util.HashSet;
import java.util.List;

import javax.annotation.Nonnull;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.atlassian.bitbucket.hook.repository.PostRepositoryHook;
import com.atlassian.bitbucket.hook.repository.PostRepositoryHookContext;
import com.atlassian.bitbucket.hook.repository.RepositoryHookRequest;
import com.atlassian.bitbucket.pull.PullRequest;
import com.atlassian.bitbucket.pull.PullRequestSearchRequest;
import com.atlassian.bitbucket.pull.PullRequestService;
import com.atlassian.bitbucket.pull.PullRequestState;
import com.atlassian.bitbucket.repository.Branch;
import com.atlassian.bitbucket.repository.RefChange;
import com.atlassian.bitbucket.repository.RefService;
import com.atlassian.bitbucket.util.Page;
import com.atlassian.bitbucket.util.PageUtils;
import com.atlassian.plugin.spring.scanner.annotation.imports.ComponentImport;

@Component
public class CommitHook implements PostRepositoryHook<RepositoryHookRequest> {

	private static final Logger log = LoggerFactory.getLogger(CommitHook.class);

	private final PullRequestService pullRequestService;

	private final RefService refService;

	@Autowired
	public CommitHook(
			@ComponentImport final PullRequestService pullRequestService,
			@ComponentImport final RefService refService) {
		this.pullRequestService = pullRequestService;
		this.refService = refService;
	}

	@Override
	public void postUpdate(
			@Nonnull final PostRepositoryHookContext context,
			@Nonnull final RepositoryHookRequest request) {

		final String targetBranch = context.getSettings().getString("target");
		final Branch defaultBranch = refService.getDefaultBranch(request.getRepository());
		final List<String> branchesToIgnore = Arrays.asList("refs/heads/develop", "refs/heads/master", defaultBranch.getId());

		request.getRefChanges().stream()
				.filter(refChange -> !branchesToIgnore.contains(refChange.getRef().getId()))
				.filter(refChange -> !openPullRequestExists(refChange.getRef().getId()))
				.forEach(refChange -> createPullRequest(request, refChange, targetBranch));

	}

	private boolean openPullRequestExists(final String refId) {
		final PullRequestSearchRequest pullRequestSearchRequest = new PullRequestSearchRequest.Builder().fromRefId(refId).state(PullRequestState.OPEN).build();
		final Page<PullRequest> found = pullRequestService.search(pullRequestSearchRequest, PageUtils.newRequest(0, 1));
		log.info(">>> {} open pullrequest(s) for {} found", found.getSize(), refId);
		return found.getSize() > 0;
	}

	private void createPullRequest(final RepositoryHookRequest request, final RefChange refChange, final String targetBranch) {
		pullRequestService.create(
				"title",
				"description",
				new HashSet<String>(),
				request.getRepository(),
				refChange.getRef().getId(),
				request.getRepository(),
				targetBranch
		);
		log.info(">>> pullrequest for {} created", refChange.getRef().getId());
	}

}
