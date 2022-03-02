#! /bin/bash
fpath=$1
if [ -d $fpath ]
then
rm -rf $fpath
elif [ -e $fpath ]
then
rm $fpath
else
echo "$fpath is NOT a file or direstory.";
fi
if [ -a $fpath ]
then
echo "Need permission to delete" $fpath;
fi
