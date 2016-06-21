package bjoveski

import java.io.File

import org.joda.time.DateTime

/**
  * Created by bojan on 6/20/16.
  */
object Reader extends Colors {

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
}


