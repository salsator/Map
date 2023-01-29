package com.example.questmap

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.location.Location
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.core.app.ActivityCompat
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import com.example.questmap.databinding.ActivityMapsBinding
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices


class MapsActivity : AppCompatActivity(), OnMapReadyCallback {


    private lateinit var binding: ActivityMapsBinding

    private lateinit var currentLocation : Location
    private lateinit var fusedLocationProviderClient: FusedLocationProviderClient
    private val permissionCode= 101



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMapsBinding.inflate(layoutInflater)
        setContentView(binding.root)


        fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(this)

        getCurrentLocationUser()

    }

   private fun getCurrentLocationUser() {


       if (ActivityCompat.checkSelfPermission(
               this,
               Manifest.permission.ACCESS_FINE_LOCATION
           ) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(
               this,
               Manifest.permission.ACCESS_COARSE_LOCATION
           ) != PackageManager.PERMISSION_GRANTED
       ) {
           ActivityCompat.requestPermissions(this,
           arrayOf(Manifest.permission.ACCESS_FINE_LOCATION),permissionCode)

           return
       }
       fusedLocationProviderClient.lastLocation.addOnSuccessListener {
           location ->

           if (location !=null)
           {
               currentLocation = location

               val mapFragment = supportFragmentManager
                   .findFragmentById(R.id.map) as SupportMapFragment
               mapFragment.getMapAsync(this)
           }
       }
    }

    //první vykreslení
    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        when(requestCode)
        {
            permissionCode -> if(grantResults.isNotEmpty() && grantResults[0]== PackageManager.PERMISSION_GRANTED)
            {
                getCurrentLocationUser()
            }
        }
    }


    override fun onMapReady(googleMap: GoogleMap) {


        val latlng = LatLng(currentLocation.latitude, currentLocation.longitude)
        val markerOptions = MarkerOptions()
                             .position(latlng)
                             .title("current Location")

        googleMap.animateCamera(CameraUpdateFactory.newLatLng(latlng))
        googleMap.animateCamera(CameraUpdateFactory.newLatLngZoom(latlng, 10f))
        googleMap.addMarker(markerOptions)



        googleMap.setOnMarkerClickListener {
            val intent = Intent (this, LoginActivity::class.java)
                this.startActivity(intent)
            true
        }


    }
}


