package com.example.android.politicalpreparedness.representative

import android.Manifest
import android.annotation.SuppressLint
import android.app.AlertDialog
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.location.Criteria
import android.location.Geocoder
import android.location.Location
import android.location.LocationManager
import android.net.Uri
import android.os.Bundle
import android.provider.Settings
import android.view.*
import android.view.inputmethod.InputMethodManager
import android.widget.AdapterView
import android.widget.ArrayAdapter
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import com.example.android.politicalpreparedness.R
import com.example.android.politicalpreparedness.databinding.FragmentRepresentativeBinding
import com.example.android.politicalpreparedness.network.models.Address
import com.example.android.politicalpreparedness.representative.adapter.RepresentativeListAdapter
import kotlinx.coroutines.launch
import org.koin.androidx.viewmodel.ext.android.viewModel
import java.util.Locale

class RepresentativeFragment : Fragment() {

    private lateinit var binding: FragmentRepresentativeBinding
    private val viewModel: RepresentativeViewModel by viewModel()
    private var permissionDialog: AlertDialog? = null

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        // Establish bindings
        binding = FragmentRepresentativeBinding.inflate(inflater, container, false)
        binding.lifecycleOwner = viewLifecycleOwner
        binding.viewModel = viewModel

        // Define and assign Representative adapter
        val adapter = RepresentativeListAdapter()
        binding.rvRepresentative.adapter = adapter

        // Populate Representative adapter
        lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.representativesDataFlow.collect {
                    adapter.submitList(it)
                    hideKeyboard()
                }
            }
        }

        ArrayAdapter.createFromResource(
            requireContext(),
            R.array.states,
            android.R.layout.simple_spinner_item
        ).also { arrayAdapter ->
            arrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
            binding.state.adapter = arrayAdapter
        }
        binding.state.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(
                adapterView: AdapterView<*>?,
                view: View?,
                pos: Int,
                id: Long
            ) {
                viewModel.setAddressState(adapterView?.getItemAtPosition(pos)?.toString() ?: "")
            }

            override fun onNothingSelected(p0: AdapterView<*>?) {}
        }

        // Establish button listeners for field and location search
        binding.buttonLocation.setOnClickListener {
            hideKeyboard()
            if (isPermissionGranted()) {
                getLocation()
            } else {
                enableLocationPermissions()
            }
        }

        return binding.root
    }

    private val requestPermissionLauncher =
        registerForActivityResult(
            ActivityResultContracts.RequestPermission()
        ) { isGranted: Boolean ->
            if (isGranted) {
                // Handle location permission result to get location on permission granted
                getLocation()
            } else {
                showPermissionDialog()
            }
        }

    private fun openPermissionSetting() {
        val intent = Intent()
        intent.action = Settings.ACTION_APPLICATION_DETAILS_SETTINGS
        val uri: Uri = Uri.fromParts("package", requireActivity().packageName, null)
        intent.data = uri
        requireActivity().startActivity(intent)
    }

    private fun showPermissionDialog() {
        if (permissionDialog == null) {
            val builder = AlertDialog.Builder(requireContext())
            builder.setTitle(getString(R.string.location_required_error))
            builder.setMessage(getString(R.string.permission_denied_explanation))

            builder.setPositiveButton(R.string.settings) { dialog, _ ->
                openPermissionSetting()
                dialog.dismiss()
            }

            builder.setNegativeButton(android.R.string.cancel) { dialog, _ ->
                dialog.dismiss()
                requireActivity().finish()
            }
            permissionDialog = builder.show()
        } else {
            permissionDialog?.show()
        }
    }

    private fun enableLocationPermissions() {
        when {
            isPermissionGranted() -> {
                // You can use the API that requires the permission.
                getLocation()
            }
            shouldShowRequestPermissionRationale(Manifest.permission.ACCESS_FINE_LOCATION) -> {
                showPermissionDialog()
            }
            else -> {
                // You can directly ask for the permission.
                // The registered ActivityResultCallback gets the result of this request.
                requestPermissionLauncher.launch(
                    Manifest.permission.ACCESS_FINE_LOCATION
                )
            }
        }
    }

    private fun isPermissionGranted(): Boolean {
        return ContextCompat.checkSelfPermission(
            requireContext(),
            Manifest.permission.ACCESS_FINE_LOCATION
        ) == PackageManager.PERMISSION_GRANTED
    }

    @SuppressLint("MissingPermission")
    private fun getLocation() {
        //Get location from LocationServices
        val locationManager = requireActivity().getSystemService(Context.LOCATION_SERVICE) as LocationManager
        val provider = locationManager.getBestProvider(Criteria(), true)
        provider?.let {
            val location = locationManager.getLastKnownLocation(it)
            if (location != null) {
                // get address from location
                geoCodeLocation(location)?.let { address ->
                    viewModel.setAddress(address)
                }
            }
        }
    }

    private fun geoCodeLocation(location: Location): Address? {
        val geocoder = Geocoder(requireContext(), Locale.getDefault())
        return geocoder.getFromLocation(location.latitude, location.longitude, 1)
            ?.map { address ->
                Address(
                    address.thoroughfare ?: "",
                    address.subThoroughfare ?: "",
                    address.locality ?: "",
                    address.adminArea ?: "",
                    address.postalCode ?: ""
                )
            }
            ?.firstOrNull()
    }

    private fun hideKeyboard() {
        val imm = activity?.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        imm.hideSoftInputFromWindow(requireView().windowToken, 0)
    }

}