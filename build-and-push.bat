@echo off

echo Building Docker image...
docker build -t cyboravidell/hisab-kitab-backend:1.1.0 .

echo Pushing to Docker Hub...
docker push cyboravidell/hisab-kitab-backend:1.1.0

echo Image built and pushed successfully!
pause