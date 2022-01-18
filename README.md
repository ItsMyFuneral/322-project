# 322-project

## Group Members

Team-04: Ryan Bowler, Jordan Colledge, Joonsik Kim, Karen Masuda, Nathan Wright

## Project Setup For Eclipse

1. Install Eclipse and a JDK.

2. On the repository page, click the Code button. Click HTML if it isn't already selected, then copy the URI from the box.

3. Go to https://github.com/settings/tokens and generate a new token with no (or 90-day) expiration. Give it gist, workflow and repo perms. Copy the token and note it down.

4. To import the repo into Eclipse:

  - a. In Eclipse, navigate to File -> Import...
  - b. Git -> Project from Git (with smart import) -> Next
  - c. Clone URI -> Next
  - d. Paste the URI in the "URI" box if it's empty. Input your GitHub username under User and the token from the previous step under Password. Check off "Store in Secure Store" and hit Next.
  - e. Make sure the only branch selected is Main -> Next
  - f. Choose the Git directory you want to use (default is C:\Users\[username]\git\322-project) -> Next -> Finish.
  
5. To push and pull files:
  - a. In Eclipse, navigate to Window -> Show View -> Other...
  - b. Under Git, choose both Git Repositories and Git Staging, then hit Open.
  - c. To push changes: Under Git Staging, drag any Unstaged Changes down to Staged Changes, add a commit message, then push and commit.
  - d. To pull changes: Under Git Repositories, right-click 322-project and select Pull.

6. Test the project to see if you're able to run it. If not, in the Git Repositories window, right-click the project repository -> Delete Repository. Delete the repository data, the working tree and remove it from the workspace.
  - a. Then, look for the "Fix Maven Dependencies Error" email and follow its instructions, re-importing the project as in Step 4.

7. Pull changes, then right-click the project -> Maven -> Update Project... -> Check "Force Update of Snapshots/Releases"
  
8. To run the code: open COSC322Test.java in Eclipse, then navigate to Run -> Run Configurations. Select COSC322Test, go to Arguments, and under "Program Arguments" input the desired username and password separated by spaces. (Currently, any can be used, but this may change in future.)
  
8. If you have any issues, contact Jordan on Discord at @nitrodog96#0886.

## Project Setup For VSCode

1. Install VSCode and a JDK.

2. Install Git. All options can be left as their default selection. https://git-scm.com/downloads
  - a. Open the Git CMD app
  - b. Type git config --global user.name "*your github username*"
  - c. Type git config --global user.email "*your github email*"

3. Under the extensions tab in VSCode, install Git Extension Pack.

4. Under the Source Control tab in VSCode, clone a repository
  - a. Paste the link to the project repository
  - b. Select where you want to store the project on your computer

5. To push and pull files
  - a. Under the Source Control tab in VSCode, click the Source Control dropdown tab after making any changes
  - b. Hover over any edited file and click the Stage Changes button (+)
  - c. Hover over the Source Control dropdown tab and click the checkmark to commit the changed files to the branch
  - d. Under the Commits dropdown menu, click push (arrow pointing up) over your recently comitted changes (highlighted green)
  - e. Hover over the Commits dropdown menu and click pull (arrow pointing down)