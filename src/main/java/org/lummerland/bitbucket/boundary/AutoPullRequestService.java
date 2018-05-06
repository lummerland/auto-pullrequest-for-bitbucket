package org.lummerland.bitbucket.boundary;

import com.atlassian.bitbucket.hook.repository.RepositoryHookRequest;

public interface AutoPullRequestService {

	void create(RepositoryHookRequest request, String targetBranch);

}
