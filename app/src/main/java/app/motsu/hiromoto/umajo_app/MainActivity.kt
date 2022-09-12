package app.motsu.hiromoto.umajo_app

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import app.motsu.hiromoto.umajo_app.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {

    lateinit var binding:ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater).apply { setContentView(this.root) }
    }
}