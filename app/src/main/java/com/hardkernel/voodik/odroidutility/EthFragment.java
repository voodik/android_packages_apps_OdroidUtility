package com.hardkernel.voodik.odroidutility;

import android.content.Context;
import android.net.EthernetManager;
import android.net.IpConfiguration;
import android.net.IpConfiguration.IpAssignment;
import android.net.IpConfiguration.ProxySettings;
import android.net.LinkAddress;
import android.net.NetworkUtils;
import android.net.Proxy;
import android.net.ProxyInfo;
import android.net.StaticIpConfiguration;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;

import java.net.Inet4Address;
import java.net.InetAddress;
import java.util.Iterator;

public class EthFragment extends Fragment {

    protected static final String TAG = Constants.TAG;
    private Spinner mIpSettingsSpinner;
    private Spinner mProxySettingsSpinner;
    private LinearLayout mstaticip;
    private LinearLayout mProxy;
    private EditText mEditTextEthIpaddress;
    private EditText mEditTextEthGateway;
    private EditText mEditTextEthPrefix;
    private EditText mEditTextEthDns1;
    private EditText mEditTextEthDns2;
    private TextView mProxyHostView;
    private TextView mProxyPortView;
    private TextView mProxyExclusionListView;

    private IpAssignment mIpAssignment = IpAssignment.UNASSIGNED;
    private ProxySettings mProxySettings = ProxySettings.UNASSIGNED;
    private ProxyInfo mHttpProxy = null;
    private StaticIpConfiguration mStaticIpConfiguration = null;
    private EthernetManager mEthernetManager;

    public static EthFragment newInstance() {
        return new EthFragment();
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_eth, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {

        mIpSettingsSpinner = (Spinner) view.findViewById(R.id.ip_settings);
        mProxySettingsSpinner = (Spinner) view.findViewById(R.id.proxy_settings);
        mstaticip = (LinearLayout) view.findViewById(R.id.staticip);
        mProxy = (LinearLayout) view.findViewById(R.id.proxy_fields);
        mEditTextEthIpaddress = (EditText) view.findViewById(R.id.ipaddress);
        mEditTextEthGateway = (EditText) view.findViewById(R.id.gateway);
        mEditTextEthPrefix = (EditText) view.findViewById(R.id.network_prefix_length);
        mEditTextEthDns1 = (EditText) view.findViewById(R.id.dns1);
        mEditTextEthDns2 = (EditText) view.findViewById(R.id.dns2);

        mProxyHostView = (TextView) view.findViewById(R.id.proxy_hostname);
        mProxyPortView = (TextView) view.findViewById(R.id.proxy_port);
        mProxyExclusionListView = (TextView) view.findViewById(R.id.proxy_exclusionlist);


        mEthernetManager = (EthernetManager) getActivity().getSystemService(Context.ETHERNET_SERVICE);
        if (mEthernetManager != null) {
            Log.e(TAG, "Connected to EthernetManager");
        } else {
            Log.e(TAG, "Unable connect to EthernetManager");
        }

        IpConfiguration config = mEthernetManager.getConfiguration();
        if (config.getIpAssignment() == IpAssignment.STATIC) {
            mIpSettingsSpinner.setSelection(Constants.STATIC_IP);
            StaticIpConfiguration staticConfig = config.getStaticIpConfiguration();

            if (staticConfig.ipAddress != null) {
                mEditTextEthIpaddress.setText(
                        staticConfig.ipAddress.getAddress().getHostAddress());
                mEditTextEthPrefix.setText(Integer.toString(staticConfig.ipAddress
                        .getNetworkPrefixLength()));
            }

            if (staticConfig.gateway != null) {
                mEditTextEthGateway.setText(staticConfig.gateway.getHostAddress());
            }

            Iterator<InetAddress> dnsIterator = staticConfig.dnsServers.iterator();
            if (dnsIterator.hasNext()) {
                mEditTextEthDns1.setText(dnsIterator.next().getHostAddress());
            }
            if (dnsIterator.hasNext()) {
                mEditTextEthDns2.setText(dnsIterator.next().getHostAddress());
            }


        } else {
            mIpSettingsSpinner.setSelection(Constants.DHCP);
        }

        if (config != null) {
            ProxyInfo proxyProperties = config.getHttpProxy();
            if (proxyProperties != null) {
                mProxyHostView.setText(proxyProperties.getHost());
                mProxyPortView.setText(Integer.toString(proxyProperties.getPort()));
                mProxyExclusionListView.setText(proxyProperties.getExclusionListAsString());
                mProxySettingsSpinner.setSelection(Constants.PROXY_STATIC);
            }
        }

        mIpSettingsSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parentView, View selectedItemView, int position, long id) {
                if (mIpSettingsSpinner.getSelectedItemPosition() == Constants.STATIC_IP) {
                    mstaticip.setVisibility(LinearLayout.VISIBLE);
                } else {
                    mstaticip.setVisibility(LinearLayout.GONE);
                }

            }

            @Override
            public void onNothingSelected(AdapterView<?> parentView) {
            }

        });

        mProxySettingsSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parentView, View selectedItemView, int position, long id) {
                if (mProxySettingsSpinner.getSelectedItemPosition() == Constants.PROXY_STATIC) {
                    mProxy.setVisibility(LinearLayout.VISIBLE);
                } else {
                    mProxy.setVisibility(LinearLayout.GONE);
                }

            }

            @Override
            public void onNothingSelected(AdapterView<?> parentView) {
            }

        });

        MainActivity mainActivity = (MainActivity) getActivity();
        mainActivity.fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (ipAndProxyFieldsAreValid()) {
                    mEthernetManager.setConfiguration(
                            new IpConfiguration(mIpAssignment, mProxySettings,
                                    mStaticIpConfiguration, mHttpProxy));
                }
            }
        });
    }


    private boolean ipAndProxyFieldsAreValid() {
        mIpAssignment = (mIpSettingsSpinner != null &&
                mIpSettingsSpinner.getSelectedItemPosition() == Constants.STATIC_IP) ?
                IpAssignment.STATIC : IpAssignment.DHCP;

        if (mIpAssignment == IpAssignment.STATIC) {
            mStaticIpConfiguration = new StaticIpConfiguration();
            int result = validateIpConfigFields(mStaticIpConfiguration);
            if (result != 0) {
                return false;
            }
        }

        final int selectedPosition = mProxySettingsSpinner.getSelectedItemPosition();
        mProxySettings = ProxySettings.NONE;
        mHttpProxy = null;
        if (selectedPosition == Constants.PROXY_STATIC && mProxyHostView != null) {
            mProxySettings = ProxySettings.STATIC;
            String host = mProxyHostView.getText().toString();
            String portStr = mProxyPortView.getText().toString();
            String exclusionList = mProxyExclusionListView.getText().toString();
            int port = 0;
            int result = 0;
            try {
                port = Integer.parseInt(portStr);
                result = validate(host, portStr, exclusionList);
            } catch (NumberFormatException e) {
                result = R.string.proxy_error_invalid_port;
            }
            if (result == 0) {
                mHttpProxy = new ProxyInfo(host, port, exclusionList);
            } else {
                return false;
            }
        }
        return true;
    }

    private Inet4Address getIPv4Address(String text) {
        try {
            return (Inet4Address) NetworkUtils.numericToInetAddress(text);
        } catch (IllegalArgumentException | ClassCastException e) {
            return null;
        }
    }

    private int validateIpConfigFields(StaticIpConfiguration staticIpConfiguration) {
        if (mEditTextEthIpaddress == null) return 0;

        String ipAddr = mEditTextEthIpaddress.getText().toString();
        if (TextUtils.isEmpty(ipAddr)) return -1;

        Inet4Address inetAddr = getIPv4Address(ipAddr);
        if (inetAddr == null) {
            return -1;
        }

        int networkPrefixLength = -1;
        try {
            networkPrefixLength = Integer.parseInt(mEditTextEthPrefix.getText().toString());
            if (networkPrefixLength < 0 || networkPrefixLength > 32) {
                return -1;
            }
            staticIpConfiguration.ipAddress = new LinkAddress(inetAddr, networkPrefixLength);
        } catch (NumberFormatException e) {
            // Set the hint as default after user types in ip address
            mEditTextEthPrefix.setText("24");
        }

        String gateway = mEditTextEthGateway.getText().toString();
        if (TextUtils.isEmpty(gateway)) {
            try {
                //Extract a default gateway from IP address
                InetAddress netPart = NetworkUtils.getNetworkPart(inetAddr, networkPrefixLength);
                byte[] addr = netPart.getAddress();
                addr[addr.length - 1] = 1;
                mEditTextEthGateway.setText(InetAddress.getByAddress(addr).getHostAddress());
            } catch (RuntimeException ee) {
            } catch (java.net.UnknownHostException u) {
            }
        } else {
            InetAddress gatewayAddr = getIPv4Address(gateway);
            if (gatewayAddr == null) {
                return -1;
            }
            staticIpConfiguration.gateway = gatewayAddr;
        }

        String dns = mEditTextEthDns1.getText().toString();
        InetAddress dnsAddr = null;

        if (TextUtils.isEmpty(dns)) {
            //If everything else is valid, provide hint as a default option
            mEditTextEthDns1.setText("8.8.8.8");
        } else {
            dnsAddr = getIPv4Address(dns);
            if (dnsAddr == null) {
                return -1;
            }
            staticIpConfiguration.dnsServers.add(dnsAddr);
        }

        if (mEditTextEthDns2.length() > 0) {
            dns = mEditTextEthDns2.getText().toString();
            dnsAddr = getIPv4Address(dns);
            if (dnsAddr == null) {
                return -1;
            }
            staticIpConfiguration.dnsServers.add(dnsAddr);
        }
        return 0;
    }

    /**
     * validate syntax of hostname and port entries
     * @return 0 on success, string resource ID on failure
     */
    public static int validate(String hostname, String port, String exclList) {
        switch (Proxy.validate(hostname, port, exclList)) {
            case Proxy.PROXY_VALID:
                return 0;
            case Proxy.PROXY_HOSTNAME_EMPTY:
                return R.string.proxy_error_empty_host_set_port;
            case Proxy.PROXY_HOSTNAME_INVALID:
                return R.string.proxy_error_invalid_host;
            case Proxy.PROXY_PORT_EMPTY:
                return R.string.proxy_error_empty_port;
            case Proxy.PROXY_PORT_INVALID:
                return R.string.proxy_error_invalid_port;
            case Proxy.PROXY_EXCLLIST_INVALID:
                return R.string.proxy_error_invalid_exclusion_list;
            default:
                // should neven happen
                Log.e(TAG, "Unknown proxy settings error");
                return -1;
        }
    }
}
