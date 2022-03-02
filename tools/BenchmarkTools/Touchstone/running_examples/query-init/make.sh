#!/bin/bash
javac SQLProduction.java
for i in {1..16}
do
	java SQLProduction ../data/result qtemplates/$i > ../data/query/$i.sql
done

