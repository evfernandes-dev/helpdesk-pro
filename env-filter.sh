OLD_EMAIL="val@local.com"
CORRECT_NAME="Emanuel Vitor Fernandes Nascimento"
CORRECT_EMAIL="emanuelvitorfn7@gmail.com"
if [ "$GIT_AUTHOR_EMAIL" = "$OLD_EMAIL" ]; then
    export GIT_AUTHOR_NAME="$CORRECT_NAME"
    export GIT_AUTHOR_EMAIL="$CORRECT_EMAIL"
fi
if [ "$GIT_COMMITTER_EMAIL" = "$OLD_EMAIL" ]; then
    export GIT_COMMITTER_NAME="$CORRECT_NAME"
    export GIT_COMMITTER_EMAIL="$CORRECT_EMAIL"
fi
