package bjoveski

import java.io.File

import org.joda.time.DateTime

/**
  * Created by bojan on 6/20/16.
  */
object IoUtil extends Colors {

  def readImages(path: String): Seq[Image] = {
    val folder = new File(path)

    require(folder.exists(), s"folder $path does not exist.")
    require(folder.isDirectory, s"$path is not a folder!")

    val files = folder.listFiles()
    val images = files.filter { f => f.isFile && f.getName.toLowerCase.endsWith(".jpg") }

    println(yellow(s"read ${images.length} images from $path"))

    // FIXME: use createdAt instead of lastModified
    images.map(f => Image(f, new DateTime(f.lastModified())))
  }


  // moves _original in a different folders
  def cleanUp(path: String, images: Seq[Option[Image]]) {
    val folder = new File(path)
    // where we'll put the old, untouched files
    val folderWithOriginals = new File(s"$path/original")
    folderWithOriginals.mkdir()

    val files = folder.listFiles()

    val originals = files.filter { f => f.isFile && f.getName.toLowerCase.endsWith(".jpg_original") }

    originals.foreach { img =>
      val newPath = s"${folderWithOriginals.getAbsolutePath}/${img.getName.dropRight("_original".length)}"
      img.renameTo(new File(newPath))
    }

    // check if it's all good
    require(originals.length == images.count(_.isDefined),
      s"found ${originals.length} files with _original ending, but ${images.count(_.isDefined)} were updated")
  }

}

