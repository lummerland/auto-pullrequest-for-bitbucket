package org.lummerland.bitbucket.hook;

import javax.annotation.Nonnull;

import org.lummerland.bitbucket.boundary.AutoPullRequestService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.atlassian.bitbucket.hook.repository.PostRepositoryHook;
import com.atlassian.bitbucket.hook.repository.PostRepositoryHookContext;
import com.atlassian.bitbucket.hook.repository.RepositoryHookRequest;

@Component
public class CommitHook implements PostRepositoryHook<RepositoryHookRequest> {

	private final AutoPullRequestService pullRequestService;

	@Autowired
	public CommitHook(final AutoPullRequestService pullRequestService) {
		this.pullRequestService = pullRequestService;
	}

	@Override
	public void postUpdate(
			@Nonnull final PostRepositoryHookContext context,
			@Nonnull final RepositoryHookRequest request) {

		final String targetBranch = context.getSettings().getString("target");
		pullRequestService.create(request, targetBranch);
	}
}
