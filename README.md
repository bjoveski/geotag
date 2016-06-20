# geotag

## overview

Geotag is a command line tool for adding GPS metadata to jpg images. The GPS metadata is extracted from Google's history and is then matched to the timestamp of the photo using a highly sophisticated algorithm.


## inspiration

During my travels in Australia and SE Asia I wished that my camera has GPS tracker, as it would make later grouping and tagging much easier. I also learned that Google extracts and tracks the location of my phone via the [Timeline project](https://maps.googleblog.com/2015/07/your-timeline-revisiting-world-that.html). So I decided to use that information and augment my photo collection with the GPS coordinates. This project tries to automate the process.

## requirements

You'll need to install [exiftool](http://www.sno.phy.queensu.ca/~phil/exiftool/) before you can use geotag. To do so download and install this [dmg](http://www.sno.phy.queensu.ca/~phil/exiftool/ExifTool-10.20.dmg). Exiftool is the tool for managing jpg (exif) metadata.

You'll also need to download your google timeline history. To do so, go to [takeout](https://takeout.google.com/settings/takeout), select "Location History (json)", and download the archive to your computer. You'll later need to tell geotag the location of this archive.

