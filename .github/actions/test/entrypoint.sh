#! /usr/bin/env bash
set -Euxo pipefail

if [ -z "$@" ]; then
    clojure -A:test
else
    $@
fi
