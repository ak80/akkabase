#!/usr/bin/env bash

echo stopServer
xargs kill -9 < target/server.pid