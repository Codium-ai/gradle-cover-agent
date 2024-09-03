#!/bin/bash

echo "You passed $# arguments."

# Loop through all the parameters
for arg in "$@"
do
    echo "$arg"
done