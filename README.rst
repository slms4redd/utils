============
AppAreaLayer
============

A utility for building classification layers.


========
AppStats
========

The slms4redd statistics engine.


========
GeoStore
========

The Geostore model used in all apps of slms4redd project.


============
MODIS Script
============

A set of python script used to download Modis Data.

===========
OnlineStats
===========

A WPS process that expose the APPSTATS engine. 

It has been tested on geoserver 2.2 

You can use the RequestBuilder (a Demo of Geoserver) to test process invocation.


Installing OnlineStats extension in GeoServer
---------------------------------------------

1. Install GeoServer 2.2.
2. Install its official WPS Extension.
3. Build the OnlineStats project::

	cd onlinestats
	mvn install

4. Get its dependencies, running::

	mvn dependency:copy-dependencies

5. Copy from ``target/dependency`` to GeoServer's ``WEB-INF/lib`` the jars that aren't already there::

	commons-cli-1.2.jar
	gt-sample-data-8.0.jar
	hamcrest-core-1.1.jar
	junit-4.10.jar
	jt-classifiedstats-1.2-GAEZM15092011.jar
	jt-utils-1.2-GAEZM15092011.jar
	teststats-1.1-SNAPSHOT.jar

.. note:: This dependency list can change if onlinestats project evolves; ``diff`` command will help sorting out the needed ones.

6. Of course, copy also the onlinestats code from ``target`` to ``WEB-INF/lib``::

	onlinestats-1.1-SNAPSHOT.jar

.. warning:: The following files could conflict. Delete the original one from GeoServer, if needed:

   * GeoServer: jt-utils-1.2.0.jar
   * AppStats: jt-utils-1.2-GAEZM15092011.jar

============
common-tests
============

Project with common classes used to tests. Initially it contains just an interface to mark the tests that require
external software to work (geoserver, geobatch, ...).
