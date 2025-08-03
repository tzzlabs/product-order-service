Git & GitHub Cheat Sheet

1. GitHub Account Setup

Sign up at GitHub with a username (e.g., tzzlabs) and verify your email.

2. Install & Configure Git (one-time)

git --version
# if missing, install from https://git-scm.com

git config --global user.name "Your Name or GitHub Username"
git config --global user.email "your_email@example.com"
# optional: set default branch name
git config --global init.defaultBranch main

3. SSH Key Setup (recommended over HTTPS)

# Generate a new key (use your email)
ssh-keygen -t ed25519 -C "your_email@example.com"
# Start agent
eval "$(ssh-agent -s)"
# Add private key
ssh-add ~/.ssh/id_ed25519
# Show public key to copy
cat ~/.ssh/id_ed25519.pub

Paste the output into GitHub: Settings → SSH and GPG keys → New SSH key.

Test connection:

ssh -T git@github.com

4. Create Remote Repository on GitHub

Via web: Create new repo (e.g., product-order-service), don’t initialize with README if you already have one locally.

5. Initializing and Pushing Local Project

cd /path/to/project
# initialize repo if not already
git init
# add remote (replace with your SSH URL)
git remote add origin git@github.com:tzzlabs/product-order-service.git

# create .gitignore, README.md, example config as needed

# stage all
git add .
# commit
git commit -m "Initial commit: project skeleton with Swagger, security, persistence"
# push to main (create upstream)
git push -u origin main

6. Creating a Descriptive Checkpoint Branch

# create and switch to a new branch
git checkout -b swagger-verification-complete
# or force reset/ensure branch matches current state
# git checkout -B swagger-verification-complete

# commit changes if any
git add .
git commit -m "Checkpoint: Swagger UI working, added README and CI workflow"
# push and set upstream
git push -u origin swagger-verification-complete

7. Tagging a Version Snapshot

# create annotated tag
git tag -a v0.1 -m "Initial working snapshot with Swagger verification"
# push tag to remote
git push origin v0.1

8. Ongoing Development

# make changes, then:
git add .
git commit -m "Describe change"
git push      # pushes current branch

9. Merging into main for stable release

# switch to main
git checkout main
# merge feature/checkpoint branch
git merge swagger-verification-complete
# push updated main
git push
# optionally tag release
git tag -a v1.0 -m "Interview-ready version"
git push origin v1.0

10. Useful Inspection Commands

# show branches and their tracking
git branch -vv
# view commit graph
git log --oneline --graph --decorate
# list tags
git tag
# show details of a tag or commit
git show v0.1

Tips

Use meaningful commit messages.

Push early and use branches for features/checkpoints.

Keep secrets (passwords, credentials) out of commits; use example templates.

Include a README with setup instructions for interviews.