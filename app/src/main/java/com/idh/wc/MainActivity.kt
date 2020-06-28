package com.idh.wc

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.Manifest
import android.annotation.SuppressLint
import android.content.Context
import android.content.pm.PackageManager
import android.location.Location
import android.location.LocationManager
import android.widget.Toast
import androidx.core.app.ActivityCompat
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.model.LatLng
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {

    val PERMISSIONS = arrayOf(
        Manifest.permission.ACCESS_COARSE_LOCATION,
        Manifest.permission.ACCESS_FINE_LOCATION)



    val REQUEST_PERMISSION_CODE = 1

    val DEFAULT_ZOOM_LEVEL = 17f


    val CITY_HALL = LatLng(37.5662952, 126.9779450999994)

    var googleMap: GoogleMap? = null


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)


        mapView.onCreate(savedInstanceState)


        if (hasPermissions()){
            initMap()
        }else{

            ActivityCompat.requestPermissions(this, PERMISSIONS, REQUEST_PERMISSION_CODE)
        }


        myLocationButton.setOnClickListener { onMyLocationButtonClick() }


    }

    override fun onRequestPermissionsResult(     requestCode: Int,        permissions: Array<out String>,
        grantResults: IntArray   ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)

        initMap()
    }


     fun hasPermissions(): Boolean {

        for(permission in PERMISSIONS){
            if (ActivityCompat.checkSelfPermission(this, permission) !=PackageManager.PERMISSION_GRANTED){
                return false
            }
        }
return true
    }

    @SuppressLint("MissingPermission")
    fun initMap(){

        mapView.getMapAsync {

            googleMap = it

            it.uiSettings.isMyLocationButtonEnabled = false

            when{
                hasPermissions()->{
                    it.isMyLocationEnabled = true

                    it.moveCamera(CameraUpdateFactory.newLatLngZoom(getMyLocation(), DEFAULT_ZOOM_LEVEL))
                }else ->{
                it.moveCamera(CameraUpdateFactory.newLatLngZoom(CITY_HALL, DEFAULT_ZOOM_LEVEL))

            }
            }

        }

    }


    @SuppressLint("MissingPermission")
    private fun getMyLocation(): LatLng{

        val locationProvider:String = LocationManager.GPS_PROVIDER

        val locationManager = getSystemService(Context.LOCATION_SERVICE) as LocationManager

        val lastKnownLocation: Location = locationManager.getLastKnownLocation(locationProvider)

        return LatLng(lastKnownLocation.latitude, lastKnownLocation.longitude)

    }


    fun onMyLocationButtonClick(){
        when{
            hasPermissions() -> googleMap?.moveCamera(CameraUpdateFactory.newLatLngZoom(getMyLocation(), DEFAULT_ZOOM_LEVEL))
            else -> Toast.makeText(applicationContext, "위치사용권한설정에 동의해주세요.", Toast.LENGTH_LONG).show()
        }
    }

    override fun onResume() {
        super.onResume()
        mapView.onResume()

    }

    override fun onPause() {
        super.onPause()
        mapView.onPause()
    }

    override fun onDestroy() {
        super.onDestroy()
        mapView.onDestroy()
    }

    override fun onLowMemory() {
        super.onLowMemory()
        mapView.onLowMemory()
    }

}
