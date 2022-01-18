# 322-project

# Group Members

Team-04: Ryan Bowler, Jordan Colledge, Joonsik Kim, Karen Masuda, Nathan Wright

# Project Setup

1. Install Eclipse and a JDK.

2. On the repository page, click the Code button. Click HTML if it isn't already selected, then copy the URI from the box.

3. Go to https://github.com/settings/tokens and generate a new token with no (or 90-day) expiration. Give it gist, workflow and repo perms. Copy the token and note it down.

4. To import the repo into Eclipse:

  a. In Eclipse, navigate to File -> Import...
  b. Git -> Project from Git (with smart import) -> Next
  c. Clone URI -> Next
  d. Paste the URI in the "URI" box if it's empty. Input your GitHub username under User and the token from the previous step under Password. Check off "Store in Secure Store" and hit Next.
  e. Next -> Choose the Git directory you want to use (default is C:\Users\[username]\git\322-project) -> Next -> Finish.
  
5. To push and pull files:
  a. In Eclipse, navigate to Window -> Show View -> Other...
  b. Under Git, choose both Git Repositories and Git Staging, then hit Open.
  c. To push changes: Under Git Staging, drag any Unstaged Changes down to Staged Changes, add a commit message, then push and commit.
  d. To pull changes: Under Git Repositories, right-click 322-project and select Pull.
  
6. To run the code: open COSC322Test.java in Eclipse, then navigate to Run -> Run Configurations. Select COSC322Test, go to Arguments, and under "Program Arguments" input the desired username and password separated by spaces. (Currently, any can be used, but this may change in future.)
  
7. If you have any issues, contact Jordan on Discord at @nitrodog96#0886.
