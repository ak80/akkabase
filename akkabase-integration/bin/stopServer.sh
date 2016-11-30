#!/usr/bin/env bash

echo stopServer
cat target/server.pid | xargs kill -9