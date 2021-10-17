[![Master](https://github.com/dodie/srtm-map-renderer/actions/workflows/master.yml/badge.svg)](https://github.com/dodie/srtm-map-renderer/actions/workflows/master.yml)
[![Quality Gate Status](https://sonarcloud.io/api/project_badges/measure?project=dodie_srtm-map-renderer&metric=alert_status)](https://sonarcloud.io/dashboard?id=dodie_srtm-map-renderer)

# SRTM Map Renderer

Render relief map and contour lines generator based on data from the [NASA Shuttle Radar Topography Mission (SRTM)](https://www2.jpl.nasa.gov/srtm/cbanddataproducts.html).

# Download the HGT files

1. Login on https://search.earthdata.nasa.gov/. Create an Earth Data account if you don't already have one.
2. Select region using the rectangle tool
3. Select HGT format
4. Download tiles for "NASA Shuttle Radar Topography Mission Global 1 arc second V003" (for example, tiles for Hungary are from N44E016 to N48E022)
5. Unzip all archives

![image](https://user-images.githubusercontent.com/1114220/136703447-768805b7-7018-4b4f-90e8-e470583a2960.png)

In the end HGT files should reside in a directory:

```
--- <path-to-hgt-data> » ls -1
N44E016.hgt
N44E017.hgt
N44E018.hgt
N44E019.hgt
N44E020.hgt
N44E021.hgt
N44E022.hgt
N45E016.hgt
...
N48E016.hgt
N48E017.hgt
N48E018.hgt
N48E019.hgt
N48E020.hgt
N48E021.hgt
N48E022.hgt
```

See all tiles of the SRTM data [here](https://www2.jpl.nasa.gov/srtm/images/SRTM_2-24-2016.gif).

# Build

```
./mvnw clean install
```

# Run

The application supports multiple map types. 

The first argument is the map type to be rendered, the second is a path that has to point to the directory where the HGT files reside.
Other arguments are specific to the selected map type.

```
java -jar target/hgt-map-renderer-0.0.1-SNAPSHOT.jar "<map type>" "<path-to-hgt-data>" [<command specific arguments>]
```


## Relief Map

The following command renders a relief map based the rectangular region from N44E016 to N48E022 and puts a red marker to indicate the followng position:
- Latitude: 47.7708946
- Longitude: 18.9205236

```
+---------+--+--+--+--+--+---------+
|         |  |  |  |  |  | N48E022 |
+---------+--+--+--+--+--+---------+
|         |  |  |  |  |  |         |
+---------+--+--+--+--+--+---------+
|         |  |  |  |  |  |         |
+---------+--+--+--+--+--+---------+
|         |  |  |  |  |  |         |
+---------+--+--+--+--+--+---------+
| N44E016 |  |  |  |  |  |         |
+---------+--+--+--+--+--+---------+
```

```
java -jar target/hgt-map-renderer-0.0.1-SNAPSHOT.jar "relief" "<path-to-hgt-data>" "47.7708946" "18.9205236" "44" "16" "48" "22"
```

![Relief map](https://github.com/dodie/hgt-map-renderer/blob/master/docs/reliefmap.png "Relief map")

## Contour lines

This command creates a profile view of a mouuntainside at position 47.7708946 / 18.9205236 with 90 degree FOV, looking South:

```
java -jar target/hgt-map-renderer-0.0.1-SNAPSHOT.jar "contour" "<path-to-hgt-data>" "47.7708946" "18.9205236" "90" "180" "0.1"
```
  
![Contour lines](https://github.com/dodie/hgt-map-renderer/blob/master/docs/contour.png "Contour lines")

## Can-See Calculation

The "cansee" command tells you if a point in the 3D space is visible from the viewpoint.
The inputs are the two coordinates with height data: Viewpoint(lat, lon, height), WatchedPoint(lat, lon, height) in this order.

In the following example we try to see a point at 500m above Pásztó from 500m above Verpelét unsuccessfully because Kékes blocks the view:
```
java -jar target/hgt-map-renderer-0.0.1-SNAPSHOT.jar "cansee" "<path-to-hgt-data>" "44" "16" "48" "22" "47.84907" "20.19078" "500" "47.91870" "19.70804" "500"
```

Int the next example the viewpoint is on the lookout tower in Kékes, and the target is the reservoir in Nagyréde.
The point is visible.
```
java -jar target/hgt-map-renderer-0.0.1-SNAPSHOT.jar "cansee" "<path-to-hgt-data>" "44" "16" "48" "22" "47.87231" "20.00913" "1050" "47.78333" "19.83700" "200"
```

Warning! This method does not yet calculate with the Earth's curve. 