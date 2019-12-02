package ollendev.aoc

import java.io.File

class FileLoader {
    companion object {
        fun getFile(resourceName: String): File {
            val fileUri = FileLoader::class.java.classLoader.getResource("ram.txt").toURI()
            return  File(fileUri)
        }
    }
}