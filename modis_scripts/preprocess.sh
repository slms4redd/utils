# Creates ingestable LayerUpdates from MODIS raw data

for indir in in/*
do
  echo " > ===================================="
  echo " > Processing $indir"
  echo " > ===================================="
  echo " >> Extracting HDF NDVI sets to GeoTIFF"
  #for file in $indir/*.hdf
  #do
    # Convert to geotiff
    #gdal_translate "HDF4_EOS:EOS_GRID:$file:MODIS_Grid_16DAY_250m_500m_VI:250m 16 days NDVI" $file.tif
  #done

  # Merge into a single file
  echo " >> Merging to single GeoTIFF"
  #gdal_merge.py -n "-3000" -o $indir/merged.tif $indir/*.tif

  # Determine date
  date=${indir//[![:digit:].]/}
  if [[ $date =~ ([0-9]{4}).([0-9]{2}).([0-9]{2}) ]]
  then
    year=${BASH_REMATCH[1]}
    month=${BASH_REMATCH[2]}
    day=${BASH_REMATCH[3]}
    date="$year-$month-$day"
  fi

  # set output dir and filename
  outdir="out/$date"
  outfile="$outdir/data/paraguay_ndvi_$year-$month-$day.tif"
  mkdir $outdir
  mkdir $outdir/data

  # Create output geotiff: Reproject, cut, optimize
  echo " >> Writing final GeoTIFF (reproject to 4626, cut to Paraguay)"
  #gdalwarp -s_srs '+proj=sinu +R=6371007.181 +nadgrids=@null +wktext' -t_srs EPSG:4326 -co "TILED=YES" -co "BLOCKXSIZE=512" -co "BLOCKYSIZE=512" -crop_to_cutline -cutline mask/paraguay.shp $indir/merged.tif $outfile

  # Add overviews
  echo " >> Adding internal Overviews"
  #gdaladdo -r average --config GDAL_TIFF_OVR_BLOCKSIZE 512 $outfile 2 4 8

  # Generate info.xml
  echo " >> Generating info.xml"
  cp info_template.xml $outdir/info.xml
  sed -i~ -e "s/_YEAR_/$year/g" $outdir/info.xml
  sed -i~ -e "s/_MONTH_/$month/g" $outdir/info.xml 
  sed -i~ -e "s/_DAY_/$day/g" $outdir/info.xml
  rm $outdir/info.xml~

  # Create zip file for ingestion
  echo " >> Creating ingestable zipfile"
  cd $outdir
  zip -r ../$date.zip .
  cd -

done
echo " > ========================================"
echo " > Done. Results are in \"out\" dir. Enjoy."
echo " > ========================================"

