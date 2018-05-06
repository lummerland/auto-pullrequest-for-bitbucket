# Auto Pull Request For Bitbucket

Sometimes it's helpful to review someones code just in time.
Normally this is what pair programming is for, yes. But to pair is not allways practical or wanted or sometimes there are just more people who have to review new code.
However, this hook for Bitbucket Server automatically opens a Pull Requests right after the first commit into the branch.

## Configuration

You can activate this hook on project or repository level. Define the branch all branches have to be merged to.

## Good to know

Pull Requests are only created for branches that come from the configurated target branch.

## Ideas for the future

* define default reviewers
* think about descriptions
