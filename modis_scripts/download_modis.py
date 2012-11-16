#!/usr/bin/env python

from datetime import *
import sys
import getopt
from ftplib import FTP
import ftplib
import logging
import socket
import re
import os


debug = True
logging.basicConfig(level = logging.DEBUG,
                    format='%(message)s',
                    filemode='w')
logger = logging.getLogger()

def usage():
  print
  print "usage: ", sys.argv[0], "-s start-date -t tiles -u ftp-server-url -o output-dir -d ftp-dir -U ftp-user -P ftp-password"
  print "  tiles is a comma separated list of tiles (i.e. h11v10,h12v10,h13v10)"
  print "  ftp-server-url for USGS: e4ftl01.cr.usgs.gov"
  print "  ftp-dir example: MOLT/MOD13Q1.005"
  print
  sys.exit(2)


def main():
  sStartDate = None
  tiles = None

  opts, extraparams = getopt.getopt(sys.argv[1:], 's:t:u:o:d:U:P:')

  sStartDate  = None
  tiles       = None
  url         = None
  outDir      = None
  ftpDir      = None
  ftpUser     = None
  ftpPassword = None

  #print 'Opts:', opts
  #print 'Extra parameters:', extraparams
  for o ,v in opts:
    if o == '-s': sStartDate  = v # download data from this date to today
    if o == '-t': tiles       = v # tiles to be downloaded
    if o == '-u': url         = v # FTserver URL i.e. e4ftl01.cr.usgs.gov
    if o == '-o': outDir      = v # Output directory
    if o == '-d': ftpDir      = v # FTP directory - i.e. MOLT/MOD13Q1.005
    if o == '-U': ftpUser     = v # FTP user
    if o == '-P': ftpPassword = v # FTP password

  if (not (sStartDate and tiles and url and outDir and ftpDir and ftpUser and ftpPassword)):
    usage()

  #downloader = downModis(sStartDate, tiles.split(','), url, 'MOLT/MOD13Q1.005', 'Anonymous', 'cicciobrutto@me.com', outDir);
  downloader = downModis(sStartDate, tiles.split(','), url, ftpDir, ftpUser, ftpPassword, outDir);
  dates = downloader.getDates();
  downloader.downloadTiles(dates);


""" Modis data download class """
class downModis:
  def __init__(self, startDate, tiles, url, dir, user, password, outDir):
    self.startDate = self.parseDate(startDate);
    self.tiles = tiles
    self.outDir = outDir

    self.ftp = modisFtpClient(url, dir, user, password)
    self.ftp.connect()

  """ parses a YYYY-DD-MM formatted date """
  def parseDate(self, dateStr):
    """Return a date object from a string"""
    dateSplit = dateStr.split('-')
    return date(int(dateSplit[0]), int(dateSplit[1]), int(dateSplit[2]))

  """ Returns today's date """
  def getToday(self):
    self.today = date.today()

  """ Returns all dates after the startDate specified in the constructor """
  def getDates(self):
    dates = self.ftp.listDates()
    return [d for d in dates if d >= self.startDate]

  """ Downloads all tiles for all dates """
  def downloadTiles(self, dates):
    for d in dates:
      strDate = d.strftime("%Y.%m.%d")
      dirPath = os.path.join(self.outDir, strDate)
      # if directory exists, skip (already downloaded)
      if not os.path.exists(dirPath):
        os.mkdir(dirPath);

      allTiles = self.ftp.listTilesInDate(d)

      for tile in self.tiles:
        logger.info('will download tile: time = ' + strDate + '; tile = ' + tile)
        regex = re.compile('%s.*\.hdf$'%tile)
        selectedTiles = [t for t in allTiles if regex.search(t)]

        if len(selectedTiles) == 0:
          continue

        tileFileName = selectedTiles[0]

        writeFilePath = os.path.join(dirPath, tileFileName)
        self.ftp.downloadTile(strDate, tileFileName, writeFilePath)

  """ Disconnects from the FTP server """
  def disconnect(self):
    self.ftp.disconnect()


""" FTP client with specific functionalities for the MODIS data servers """
class modisFtpClient:
  def __init__(self, url, path, user, password):
    self.url      = url
    self.path     = path
    self.user     = user
    self.password = password

  """ Connects to the FTP server """
  def connect(self):
    """ Set connection to ftp server, move to path where data are stored
    and create a list of directory for all days"""
    try:
      # connect to ftp server
      self.ftp = FTP(self.url)
      self.ftp.login(self.user, self.password)
      if debug == True:
        logger.debug("Open connection %s" % self.url)
    except EOFError:
      logger.error('Error in connection')

  """ Lists all the dates available """
  def listDates(self):
    # enter in directory
    self.ftp.cwd(self.path)
    dirData = []

    # return data inside directory
    self.ftp.dir(dirData.append)

    # check if dirData contain only directory, delete all files
    strDates = [elem.split()[-1] for elem in dirData if elem.startswith("d")]

    dateSplits = [dateStr.split('.') for dateStr in strDates]

    dates = [date(int(dateSplit[0]), int(dateSplit[1]), int(dateSplit[2])) for dateSplit in dateSplits]
    dates.sort();

    return dates

  """ Lists all tiles for a given date """ 
  def listTilesInDate(self, date):
    try:
      self.ftp.cwd(date.strftime("%Y.%m.%d"))

      # return data inside directory
      dirData = self.ftp.nlst()

      self.ftp.cwd('..')

      return dirData
    except (ftplib.error_reply, socket.error), e:
      logging.error("Error %s when try to receive list of files" % e)
      #self.getFilesList()

  """ Downloads a single tile - skips if exists """
  def downloadTile(self, dateDir, tileFileName, writeFilePath):
    print writeFilePath
    if os.path.exists(writeFilePath):
      logger.info('file ' + writeFilePath + ' exitst - skipping')
      return

    try:
      file_hdf = open(writeFilePath, "wb")
      self.ftp.retrbinary("RETR " + dateDir + '/' + tileFileName, file_hdf.write)
    except (ftplib.error_reply, socket.error), e:
      logging.error("Error downloading tile: " + tileFileName)

  """ Disconnects from the FTP server """
  def disconnect(self):
    """ Close ftp connection """
    self.ftp.quit()
    #self.filelist.close()
    if debug == True:
      logger.debug("Close connection %s" % self.url)


if __name__ == "__main__":
  main()
