package com.example.usb_android
import android.content.Context
import android.hardware.usb.UsbConstants
import android.hardware.usb.UsbManager
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.ArrayAdapter
import android.widget.ListView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity

class MainActivity : AppCompatActivity() {

    private lateinit var usbManager: UsbManager

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        usbManager = getSystemService(Context.USB_SERVICE) as UsbManager
    }

    fun checkOtg(view: View) {
        val usbDevices = usbManager.deviceList
        if (usbDevices.isNotEmpty()) {
            val deviceNames = ArrayList<String>()
            for (device in usbDevices.values) {
                for (i in 0 until device.interfaceCount) {
                    val usbInterface = device.getInterface(i)
                    val interfaceClass = usbInterface.interfaceClass
                    val interfaceSubclass = usbInterface.interfaceSubclass
                    val interfaceProtocol = usbInterface.interfaceProtocol

                    // Identificar el tipo de dispositivo USB
                    val deviceType = when (interfaceClass) {
                        UsbConstants.USB_CLASS_HID -> {
                            when {
                                interfaceSubclass == UsbConstants.USB_INTERFACE_SUBCLASS_BOOT && interfaceProtocol == 0x01 -> "Teclado"
                                interfaceSubclass == UsbConstants.USB_INTERFACE_SUBCLASS_BOOT && interfaceProtocol == 0x02 -> "Ratón"
                                else -> "Dispositivo HID"
                            }
                        }
                        UsbConstants.USB_CLASS_MASS_STORAGE -> "Almacenamiento masivo"
                        else -> "Dispositivo desconocido"
                    }

                    deviceNames.add("Nombre del dispositivo: ${device.deviceName}\n" +
                            "Tipo de dispositivo: $deviceType \n" +"Producto: ${device.productName}" )

                    Log.d("OTG", "Nombre del dispositivo: ${device.deviceName}")
                    Log.d("OTG", "Producto: ${device.productName}")
                    Log.d("OTG", "Tipo de dispositivo: $deviceType")
                }
            }
            val deviceListAdapter = ArrayAdapter(this, android.R.layout.simple_list_item_1, deviceNames)
            val deviceList = findViewById<ListView>(R.id.deviceList)
            deviceList.adapter = deviceListAdapter
            Toast.makeText(this, "Tu dispositivo admite OTG", Toast.LENGTH_SHORT).show()
        } else {
            Toast.makeText(this, "Tu dispositivo no admite OTG", Toast.LENGTH_SHORT).show()
        }
    }
}
