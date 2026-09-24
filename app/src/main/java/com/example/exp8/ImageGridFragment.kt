package com.example.exp8

import android.graphics.Bitmap
import android.graphics.Canvas
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import java.io.File
import java.io.FileOutputStream

class ImageGridFragment : Fragment() {

    private lateinit var recyclerView: RecyclerView
    private lateinit var adapter: ImageAdapter
    private var targetItemPositionToPick = -1

    private val pickLocalDeviceImageLauncher = registerForActivityResult(
        ActivityResultContracts.GetContent()
    ) { uri: Uri? ->
        if (uri != null && targetItemPositionToPick in imageList.indices) {
            val item = imageList[targetItemPositionToPick]
            item.filePath = uri.toString()
            adapter.notifyItemChanged(targetItemPositionToPick)
            Toast.makeText(requireContext(), "Loaded photo from local device storage: ${item.title}", Toast.LENGTH_SHORT).show()
        }
    }

    private val imageList = mutableListOf<ImageItem>()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_image_grid, container, false)
        recyclerView = view.findViewById(R.id.recyclerView)

        (activity as? AppCompatActivity)?.supportActionBar?.title = "Image Gallery Grid"
        (activity as? AppCompatActivity)?.supportActionBar?.subtitle = "1. Drawable  |  2. Local  |  3. URL"

        val localFilePath1 = createLocalImageFileFromDrawable("sample_local_1.png", R.drawable.sample_drawable_5)
        val localFilePath2 = createLocalImageFileFromDrawable("sample_local_2.png", R.drawable.sample_drawable_6)

        if (imageList.isEmpty()) {
            imageList.addAll(
                listOf(
                    // Source 1: Drawable Resources
                    ImageItem(
                        id = "1",
                        title = "Geometric Triangle",
                        sourceType = ImageSourceType.DRAWABLE,
                        drawableResId = R.drawable.sample_drawable_1
                    ),
                    ImageItem(
                        id = "2",
                        title = "Circle Ring Design",
                        sourceType = ImageSourceType.DRAWABLE,
                        drawableResId = R.drawable.sample_drawable_2
                    ),
                    // Source 2: Local Device Storage Files
                    ImageItem(
                        id = "3",
                        title = "Local Device Storage 1",
                        sourceType = ImageSourceType.LOCAL_STORAGE,
                        filePath = localFilePath1
                    ),
                    ImageItem(
                        id = "4",
                        title = "Local Device Storage 2",
                        sourceType = ImageSourceType.LOCAL_STORAGE,
                        filePath = localFilePath2
                    ),
                    // Source 3: URL / Web URI Based Images
                    ImageItem(
                        id = "5",
                        title = "Online Web Image 1",
                        sourceType = ImageSourceType.URL,
                        url = "https://picsum.photos/300/300?random=1"
                    ),
                    ImageItem(
                        id = "6",
                        title = "Online Web Image 2",
                        sourceType = ImageSourceType.URL,
                        url = "https://picsum.photos/300/300?random=2"
                    )
                )
            )
        }

        adapter = ImageAdapter(
            context = requireContext(),
            items = imageList,
            onItemClick = { item ->
                val status = if (item.isSelected) "Selected" else "Unselected"
                Toast.makeText(requireContext(), "$status: ${item.title}", Toast.LENGTH_SHORT).show()
            },
            onPickLocalImage = { position ->
                targetItemPositionToPick = position
                pickLocalDeviceImageLauncher.launch("image/*")
            }
        )

        recyclerView.layoutManager = GridLayoutManager(requireContext(), 2)
        recyclerView.adapter = adapter

        return view
    }

    fun selectAll() {
        if (::adapter.isInitialized) {
            adapter.selectAll()
        }
    }

    private fun createLocalImageFileFromDrawable(filename: String, drawableResId: Int): String {
        val file = File(requireContext().filesDir, filename)
        try {
            val drawable = ContextCompat.getDrawable(requireContext(), drawableResId)
            if (drawable != null) {
                val bitmap = Bitmap.createBitmap(
                    drawable.intrinsicWidth.takeIf { it > 0 } ?: 300,
                    drawable.intrinsicHeight.takeIf { it > 0 } ?: 300,
                    Bitmap.Config.ARGB_8888
                )
                val canvas = Canvas(bitmap)
                drawable.setBounds(0, 0, canvas.width, canvas.height)
                drawable.draw(canvas)

                FileOutputStream(file).use { out ->
                    bitmap.compress(Bitmap.CompressFormat.PNG, 100, out)
                }
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
        return file.absolutePath
    }
}
