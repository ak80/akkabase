#!/usr/bin/env bash

echo runServer $@
$@  > target/server.log 2>&1 &
echo $! > target/server.pid