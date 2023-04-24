#!/usr/bin/env pwsh
cd $PSScriptRoot
if (-not $TAG) { $TAG = "harbor.fsa.os.univ-lyon1.fr/prj-15/workercontroller:latest" }
$MIRROR_SWITCH=""
if ($MVN_MIRROR) { $MIRROR_SWITCH="--build-arg MVN_MIRROR" }
docker build $MIRROR_SWITCH -t $TAG . ; docker push $TAG