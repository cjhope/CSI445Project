#!/bin/bash:/usr/bin
echo Converting text files to tree-tagger formatted files

DIR=$( cd "$( dirname "${BASH_SOURCE[0]}" )" && pwd )
FILES=$DIR/data/rawData/fiveStars/*
OUTPUT=$DIR/data/results/fiveStars/
i=1

for f in $FILES
do
echo $f
cat $f | /Users/cjh/TreeTagger/cmd/tree-tagger-english > $DIR/data/formattedData/myResultFile$i.txt
let i++

done
