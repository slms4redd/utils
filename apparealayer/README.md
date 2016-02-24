#Apparealayer
this tool builds the DataLayer, the raster layer used in the zonal stats, see [here](https://github.com/slms4redd/utils/wiki/Statistics-General-Concepts)

##Build

```
#../utils/apparealayer$ mvn clean install
```
the build creates an executable jar called **arealayer-1.1-SNAPSHOT.one-jar.jar** packed with all dependencies in ``#../utils/apparealayer/target$``

##Run
Move the **arealayer-1.1-SNAPSHOT.one-jar.jar** wherever you want on your filesystem, open a terminal and run

```
#somewhere/your/fs$ java -jar arealayer-1.1-SNAPSHOT.one-jar.jar
```
To see the help inline 
```
ERROR 2016-02-25 00:15:58.957 class it.geosolutions.unredd.apputil.AreaBuilder::main - Parse error: org.apache.commons.cli.MissingOptionException: Missing required options: x, s, o
usage: createAreaLayer
 -?,--help                   print help
 -m,--mem <megabytes>        the max memory available for the operation
 -o,--outfile <file>         the output tiff file
 -s,--size <width,height>    size of output image in pixel in the format
                             width,height
 -t,--threads <numThreads>   number of threads JAI will use
 -x,--extents <n/e/s/w>      extents in the format n/e/s/w
```

Please note that:

* the **-m** parameters is the size of the JAI tile cache (not exactly the max memory available) and you have to set it higher as possible but of course it must be lower that the max heap size assigned to the process
* the extents **-x** is in the **n/e/s/w** form which is not the OGC boundingbox (minX,maxX,minY,maxY)

For example if **gdalinfo** return an extents like this:

```	
Upper Left  (  21.7378700,  -8.0848600) ( 21d44'16.33"E,  8d 5' 5.50"S)
Lower Left  (  21.7378700, -18.1832700) ( 21d44'16.33"E, 18d10'59.77"S)
Upper Right (  33.7303900,  -8.0848600) ( 33d43'49.40"E,  8d 5' 5.50"S)
Lower Right (  33.7303900, -18.1832700) ( 33d43'49.40"E, 18d10'59.77"S)
Center      (  27.7341300, -13.1340650) ( 27d44' 2.87"E, 13d 8' 2.63"S)
```
The bounding box in n/e/s/w is

```
-8.0848600/33.7303900/-18.1832700/21.7378700
```

###Run example

```
java -Xms3G -Xmx3G -jar "C:\Users\slms\Desktop\arealayer.jar" -m 2048  -o "D:\work\data\stats\zambia-arealayer.tif" -s 43915,36979 -t 4 -x -8.0848600/33.7303900/-18.1832700/21.7378700
```

