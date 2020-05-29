# HGT Map Renderer

Java experiment to render relief maps and contour lines based on data from the NASA Shuttle Radar Topography Mission (SRTM).

![Relief map](https://github.com/dodie/hgt-map-renderer/blob/master/docs/reliefmap.png "Relief map")

![Contour lines](https://github.com/dodie/hgt-map-renderer/blob/master/docs/contour.png "Contour lines")

# Build

```
mvn clean install
```

# Run

[Download HGT files](https://www2.jpl.nasa.gov/srtm/cbanddataproducts.html).

Run the application with the following command:

```
java -jar target/hgt-map-renderer-0.0.1-SNAPSHOT.jar /<my-path-to-hgt-data>/
```

Where the `<my-path-to-hgt-data>` specifies a directory containing HGT files, like:

```
--- <my-path-to-hgt-data> » ls -1
N44E016.hgt
N44E017.hgt
N44E018.hgt
N44E019.hgt
N44E020.hgt
N44E021.hgt
N44E022.hgt
N45E016.hgt
...
```


