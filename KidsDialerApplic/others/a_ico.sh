#!/bin/sh

#Интернет магазин Google 		512x512
#Очень высокое разрешение (xxhdpi)	144x144
#Очень высокое разрешение (xhdpi)	96x96
#Высококе разрешение (hdpi)		72x72
#Среднее разрешение (mdpi)		48x48 base
#Низкое разрешение (ldpi)		36x36

DIR=`basename $1 .png`

mkdir $DIR

[ -d $DIR ] && {
	mkdir $DIR/drawable-hdpi 
	convert -resize 72x $1 $DIR/drawable-hdpi/$1
	mkdir $DIR/drawable-ldpi 
	convert -resize 36x $1 $DIR/drawable-ldpi/$1
	mkdir $DIR/drawable-mdpi 
	convert -resize 48x $1 $DIR/drawable-mdpi/$1
	mkdir $DIR/drawable-xhdpi 
	convert -resize 96x $1 $DIR/drawable-xhdpi/$1
	mkdir $DIR/drawable-xxhdpi 
	convert -resize 144x $1 $DIR/drawable-xxhdpi/$1
}
