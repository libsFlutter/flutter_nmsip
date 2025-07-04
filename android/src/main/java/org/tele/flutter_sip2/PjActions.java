package org.tele.flutter_sip2;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;

import org.telon.sip2.dto.CallSettingsDTO;
import org.telon.sip2.dto.SipMessageDTO;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;
import java.util.Arrays;

public class PjActions {

    public static final String TAG = "PjActions";

    public static final String ACTION_START = "start";
    public static final String ACTION_CREATE_ACCOUNT = "account_create";
    public static final String ACTION_CHANGE_CODEC_SETTINGS= "change_codec_settings'";
    public static final String ACTION_REGISTER_ACCOUNT = "account_register";
    public static final String ACTION_DELETE_ACCOUNT = "account_delete";
    public static final String ACTION_MAKE_CALL = "call_make";
    public static final String ACTION_HANGUP_CALL = "call_hangup";
    public static final String ACTION_DECLINE_CALL = "call_decline";
    public static final String ACTION_ANSWER_CALL = "call_answer";
    public static final String ACTION_HOLD_CALL = "call_hold";
    public static final String ACTION_UNHOLD_CALL = "call_unhold";
    public static final String ACTION_MUTE_CALL = "call_mute";
    public static final String ACTION_UNMUTE_CALL = "call_unmute";
    public static final String ACTION_USE_SPEAKER_CALL = "call_use_speaker";
    public static final String ACTION_USE_EARPIECE_CALL = "call_use_earpiece";
    public static final String ACTION_XFER_CALL = "call_xfer";
    public static final String ACTION_XFER_REPLACES_CALL = "call_xfer_replace";
    public static final String ACTION_REDIRECT_CALL = "call_redirect";
    public static final String ACTION_DTMF_CALL = "call_dtmf";

    public static final String ACTION_SET_SERVICE_CONFIGURATION = "set_service_configuration";

    public static final String ACTION_ACTIVATEAUDIOSESSION_CALL = "call_activateaudiosession";
    public static final String ACTION_RINGING_CALL = "call_ringing";
    public static final String ACTION_PROGRESS_CALL = "call_progress";


    public static final String EVENT_STARTED = "org.telon.account.started";
    public static final String EVENT_ACCOUNT_CREATED = "org.telon.account.created";
    public static final String EVENT_REGISTRATION_CHANGED = "org.telon.registration.changed";
    public static final String EVENT_CALL_CHANGED = "org.telon.call.changed";
    public static final String EVENT_CALL_TERMINATED = "org.telon.call.terminated";
    public static final String EVENT_CALL_RECEIVED = "org.telon.call.received";
    public static final String EVENT_CALL_SCREEN_LOCKED = "org.telon.call.screen.locked";
    public static final String EVENT_MESSAGE_RECEIVED = "org.telon.message.received";
    public static final String EVENT_HANDLED = "org.telon.handled";

    public static Intent createStartIntent(int callbackId, Map<String, Object> configuration, Context context) {
        Intent intent = new Intent(context, org.telon.sip2.PjSipService.class);
        intent.setAction(PjActions.ACTION_START);
        intent.putExtra("callback_id", callbackId);

        formatIntent(intent, configuration);

        return intent;
    }

    public static Intent createSetServiceConfigurationIntent(int callbackId, Map<String, Object> configuration, Context context) {
        Intent intent = new Intent(context, org.telon.sip2.PjSipService.class);
        intent.setAction(PjActions.ACTION_SET_SERVICE_CONFIGURATION);
        intent.putExtra("callback_id", callbackId);

        formatIntent(intent, configuration);

        return intent;
    }

    public static Intent createAccountCreateIntent(int callbackId, Map<String, Object> configuration, Context context) {
        Intent intent = new Intent(context, org.telon.sip2.PjSipService.class);
        intent.setAction(PjActions.ACTION_CREATE_ACCOUNT);
        intent.putExtra("callback_id", callbackId);

        formatIntent(intent, configuration);

        return intent;
    }

    public static Intent createAccountRegisterIntent(int callbackId, int accountId, boolean renew, Context context) {
        Intent intent = new Intent(context, org.telon.sip2.PjSipService.class);
        intent.setAction(PjActions.ACTION_REGISTER_ACCOUNT);
        intent.putExtra("callback_id", callbackId);
        intent.putExtra("account_id", accountId);
        intent.putExtra("renew", renew);

        return intent;
    }

    public static Intent createAccountDeleteIntent(int callbackId, int accountId, Context context) {
        Intent intent = new Intent(context, org.telon.sip2.PjSipService.class);
        intent.setAction(PjActions.ACTION_DELETE_ACCOUNT);
        intent.putExtra("callback_id", callbackId);
        intent.putExtra("account_id", accountId);

        return intent;
    }

    public static Intent createMakeCallIntent(int callbackId, int accountId, String destination, Map<String, Object> settings, Map<String, Object> message, Context context) {
        Intent intent = new Intent(context, org.telon.sip2.PjSipService.class);
        intent.setAction(PjActions.ACTION_MAKE_CALL);
        intent.putExtra("callback_id", callbackId);
        intent.putExtra("account_id", accountId);
        intent.putExtra("destination", destination);

        if (settings != null) {
            intent.putExtra("settings", CallSettingsDTO.fromMap(settings).toJson());
        }

        if (message != null) {
            intent.putExtra("message", SipMessageDTO.fromMap(message).toJson());
        }

        return intent;
    }

    public static Intent createHangupCallIntent(int callbackId, int callId, Context context) {
        Intent intent = new Intent(context, org.telon.sip2.PjSipService.class);
        intent.setAction(PjActions.ACTION_HANGUP_CALL);
        intent.putExtra("callback_id", callbackId);
        intent.putExtra("call_id", callId);

        return intent;
    }

    public static Intent createDeclineCallIntent(int callbackId, int callId, Context context) {
        Intent intent = new Intent(context, org.telon.sip2.PjSipService.class);
        intent.setAction(PjActions.ACTION_DECLINE_CALL);
        intent.putExtra("callback_id", callbackId);
        intent.putExtra("call_id", callId);

        return intent;
    }

    public static Intent createAnswerCallIntent(int callbackId, int callId, Context context) {
        Intent intent = new Intent(context, org.telon.sip2.PjSipService.class);
        intent.setAction(PjActions.ACTION_ANSWER_CALL);
        intent.putExtra("callback_id", callbackId);
        intent.putExtra("call_id", callId);

        return intent;
    }

    public static Intent createRingingCallIntent(int callbackId, int callId, Context context) {
        Intent intent = new Intent(context, org.telon.sip2.PjSipService.class);
        intent.setAction(PjActions.ACTION_RINGING_CALL);
        intent.putExtra("callback_id", callbackId);
        intent.putExtra("call_id", callId);

        return intent;
    }

    public static Intent createProgressCallIntent(int callbackId, int callId, Context context) {
        Intent intent = new Intent(context, org.telon.sip2.PjSipService.class);
        intent.setAction(PjActions.ACTION_PROGRESS_CALL);
        intent.putExtra("callback_id", callbackId);
        intent.putExtra("call_id", callId);

        return intent;
    }

    public static Intent createHoldCallIntent(int callbackId, int callId, Context context) {
        Intent intent = new Intent(context, org.telon.sip2.PjSipService.class);
        intent.setAction(PjActions.ACTION_HOLD_CALL);
        intent.putExtra("callback_id", callbackId);
        intent.putExtra("call_id", callId);

        return intent;
    }

    public static Intent createUnholdCallIntent(int callbackId, int callId, Context context) {
        Intent intent = new Intent(context, org.telon.sip2.PjSipService.class);
        intent.setAction(PjActions.ACTION_UNHOLD_CALL);
        intent.putExtra("callback_id", callbackId);
        intent.putExtra("call_id", callId);

        return intent;
    }

    public static Intent createMuteCallIntent(int callbackId, int callId, Context context) {
        Intent intent = new Intent(context, org.telon.sip2.PjSipService.class);
        intent.setAction(PjActions.ACTION_MUTE_CALL);
        intent.putExtra("callback_id", callbackId);
        intent.putExtra("call_id", callId);

        return intent;
    }

    public static Intent createUnMuteCallIntent(int callbackId, int callId, Context context) {
        Intent intent = new Intent(context, org.telon.sip2.PjSipService.class);
        intent.setAction(PjActions.ACTION_UNMUTE_CALL);
        intent.putExtra("callback_id", callbackId);
        intent.putExtra("call_id", callId);

        return intent;
    }

    public static Intent createUseSpeakerCallIntent(int callbackId, int callId, Context context) {
        Intent intent = new Intent(context, org.telon.sip2.PjSipService.class);
        intent.setAction(PjActions.ACTION_USE_SPEAKER_CALL);
        intent.putExtra("callback_id", callbackId);
        intent.putExtra("call_id", callId);

        return intent;
    }

    public static Intent createUseEarpieceCallIntent(int callbackId, int callId, Context context) {
        Intent intent = new Intent(context, org.telon.sip2.PjSipService.class);
        intent.setAction(PjActions.ACTION_USE_EARPIECE_CALL);
        intent.putExtra("callback_id", callbackId);
        intent.putExtra("call_id", callId);

        return intent;
    }

    public static Intent createXFerCallIntent(int callbackId, int callId, String destination, Context context) {
        Intent intent = new Intent(context, org.telon.sip2.PjSipService.class);
        intent.setAction(PjActions.ACTION_XFER_CALL);
        intent.putExtra("callback_id", callbackId);
        intent.putExtra("call_id", callId);
        intent.putExtra("destination", destination);

        return intent;
    }

    public static Intent createXFerReplacesCallIntent(int callbackId, int callId, int destinationCallId, Context context) {
        Intent intent = new Intent(context, org.telon.sip2.PjSipService.class);
        intent.setAction(PjActions.ACTION_XFER_REPLACES_CALL);
        intent.putExtra("callback_id", callbackId);
        intent.putExtra("call_id", callId);
        intent.putExtra("destination_call_id", destinationCallId);

        return intent;
    }

    public static Intent createRedirectCallIntent(int callbackId, int callId, String destination, Context context) {
        Intent intent = new Intent(context, org.telon.sip2.PjSipService.class);
        intent.setAction(PjActions.ACTION_REDIRECT_CALL);
        intent.putExtra("callback_id", callbackId);
        intent.putExtra("call_id", callId);
        intent.putExtra("destination", destination);

        return intent;
    }

    public static Intent createDtmfCallIntent(int callbackId, int callId, String digits, Context context) {
        Intent intent = new Intent(context, org.telon.sip2.PjSipService.class);
        intent.setAction(PjActions.ACTION_DTMF_CALL);
        intent.putExtra("callback_id", callbackId);
        intent.putExtra("call_id", callId);
        intent.putExtra("digits", digits);

        return intent;
    }

    public static Intent createChangeCodecSettingsIntent(int callbackId, Map<String, Object> codecSettings, Context context) {
        Intent intent = new Intent(context, org.telon.sip2.PjSipService.class);
        intent.setAction(PjActions.ACTION_CHANGE_CODEC_SETTINGS);
        intent.putExtra("callback_id", callbackId);

        formatIntent(intent, codecSettings);

        return intent;
    }

    public static Intent createUpdateStunServersIntent(int callbackId, int accountId, java.util.List<String> stunServerList, Context context) {
        Intent intent = new Intent(context, org.telon.sip2.PjSipService.class);
        intent.setAction("update_stun_servers");
        intent.putExtra("callback_id", callbackId);
        intent.putExtra("account_id", accountId);
        intent.putExtra("stun_server_list", (Serializable) stunServerList);

        return intent;
    }

    public static Intent createChangeNetworkConfigurationIntent(int callbackId, Map<String, Object> configuration, Context context) {
        Intent intent = new Intent(context, org.telon.sip2.PjSipService.class);
        intent.setAction("change_network_configuration");
        intent.putExtra("callback_id", callbackId);

        formatIntent(intent, configuration);

        return intent;
    }

    private static void formatIntent(Intent intent, Map<String, Object> configuration) {
        if (configuration != null) {
            for (Map.Entry<String, Object> entry : configuration.entrySet()) {
                String key = entry.getKey();
                Object value = entry.getValue();

                if (value instanceof String) {
                    intent.putExtra(key, (String) value);
                } else if (value instanceof Integer) {
                    intent.putExtra(key, (Integer) value);
                } else if (value instanceof Boolean) {
                    intent.putExtra(key, (Boolean) value);
                } else if (value instanceof Double) {
                    intent.putExtra(key, (Double) value);
                } else if (value instanceof Map) {
                    intent.putExtra(key, (Serializable) value);
                } else if (value instanceof java.util.List) {
                    intent.putExtra(key, (Serializable) value);
                }
            }
        }
    }
}
