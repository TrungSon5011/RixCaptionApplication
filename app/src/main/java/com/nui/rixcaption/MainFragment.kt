package com.nui.rixcaption

import android.Manifest
import android.annotation.SuppressLint
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Bundle
import android.os.Environment
import android.util.Log
import android.view.*
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.core.app.ActivityCompat
import androidx.fragment.app.Fragment
import com.nui.rixcaption.util.ExecuteBinaryResponse
import com.nui.rixcaption.util.SessionManager
import com.nui.rixcaption.util.UtilObject
import nl.bravobit.ffmpeg.FFmpeg
import java.text.SimpleDateFormat
import java.util.*

private const val PICK_FILE = 1
private const val REQUEST_READ_EXTERNAL_STORAGE = 49
private const val REQUEST_WRITE_EXTERNAL_STORAGE = 50
class MainFragment: Fragment() {
    private lateinit var videoView: VideoView
    private lateinit var addText: Button
    private lateinit var textEditText : EditText
    private lateinit var dfgd: EditText
    private var xPosition : Int = 0
    private var yPosition : Int = 0
    private var videoUri : Uri? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setHasOptionsMenu(true)
    }
    @SuppressLint("ClickableViewAccessibility")
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view : View = inflater.inflate(R.layout.fragment_main,container,false)
        videoView = view.findViewById(R.id.videoView)
        addText = view.findViewById(R.id.addTextBtn)
        textEditText = view.findViewById(R.id.addTextEditText)

        var toolbar: Toolbar  = view.findViewById(R.id.tool_bar)
        (activity as AppCompatActivity?)!!.setSupportActionBar(toolbar)
        addText.setOnClickListener {
            var textString: String = textEditText.text?.toString() ?: ""
            if (textString == ""){
                Toast.makeText(requireContext(),"Enter text to add",Toast.LENGTH_SHORT).show()
            }else{
                addTextFunction(textString)
            }
        }
        videoView.setOnTouchListener(HandleTouch())
        return view
    }

    private fun addTextFunction(textString: String) {
        val ffmpeg: FFmpeg = FFmpeg.getInstance(requireContext())
        if (ActivityCompat.checkSelfPermission(requireContext(),Manifest.permission.READ_EXTERNAL_STORAGE)
        == PackageManager.PERMISSION_GRANTED &&
            ActivityCompat.checkSelfPermission(requireContext(),Manifest.permission.WRITE_EXTERNAL_STORAGE)
            == PackageManager.PERMISSION_GRANTED){

            val listString = mutableListOf<String>()
            val sessionManager = SessionManager()
            val fontFile = UtilObject.copyFontToInternalStorage(R.font.arial_black,"arial_black",
                    requireContext())

            sessionManager.setFirstTime(requireActivity(), false)

            var videoPath = UtilObject.getPath(context, videoUri)
            var outputPath = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_MOVIES).absolutePath +
                    "/" + SimpleDateFormat("ddMMyyyy_HHmmss").format(Date()) + ".mp4"

            var currentTime : Int = videoView.currentPosition / 1000
            listString.add("-i")
            listString.add("${videoPath}")
            listString.add("-vf")
            listString.add("drawtext=fontfile=${fontFile.path}: text='$textString': fontcolor=white: fontsize=40: box=1: boxcolor=black@0.5: boxborderw=5: x=$xPosition: y=$yPosition:enable='between(t,$currentTime,${currentTime+3})'")
            listString.add("-codec:a")
            listString.add("copy")
            listString.add(outputPath)

            val execute = ExecuteBinaryResponse()
            ffmpeg.execute(listString.toTypedArray(),execute)
        }else {
            requestPermission()
        }


    }
    private fun requestPermission() {
        ActivityCompat.requestPermissions(requireActivity(), arrayOf(android.Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE), REQUEST_READ_EXTERNAL_STORAGE)
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.menu_main,menu)

    }

    @SuppressLint("IntentReset")
    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (item.itemId == R.id.upload) {
            Log.i("Main","Yes")
            val intent: Intent = Intent(Intent.ACTION_PICK,android.provider.MediaStore.Video.Media.EXTERNAL_CONTENT_URI)
            intent.type = "video/*"
            startActivityForResult(intent, PICK_FILE)
        }
        return super.onOptionsItemSelected(item)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (requestCode == PICK_FILE){
            videoUri = data?.data
        }
        videoView.setVideoURI(videoUri)
        val mediaController: MediaController = MediaController(requireContext())
        mediaController.setAnchorView(videoView)
        videoView.setMediaController(mediaController)
    }

    companion object{
        fun newInstance() = MainFragment()
    }
    private inner class HandleTouch: View.OnTouchListener{
        override fun onTouch(v: View?, event: MotionEvent?): Boolean {
            xPosition = event?.x?.toInt() ?: 0
            yPosition = event?.y?.toInt() ?: 0
            when(event?.action){
                MotionEvent.ACTION_UP -> Log.i("HandleTouch","Touch Up ")
                MotionEvent.ACTION_DOWN -> Log.i("HandleTouch","Touch down $xPosition $yPosition")
                MotionEvent.ACTION_MOVE -> Log.i("HandleTouch","x: $xPosition, y: $yPosition")
            }
            return false
        }
    }

}