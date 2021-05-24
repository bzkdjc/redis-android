package io.wf9a5m75.redis;

import android.content.Context;
import android.os.Bundle;
import android.util.Log;

import java.io.File;
import java.util.ArrayList;
import java.util.Locale;

public class RedisAndroid  {
  private static final String TAG = "RedisService";

  static {
    System.loadLibrary("redis");
  }

  private static native int native_redisStart(String configs);

  @SuppressWarnings("InfiniteRecursion")
  public static void start(Context context) {
    RedisAndroid.start(context);
  }

  public static void start(Context context, Bundle options) {
    String packageName = context.getPackageName();
    String port = "6379";
    if (options != null && options.containsKey("port")) {
      port = options.get("port") + "";
    }

    Bundle configs = new Bundle();
    //-------------------
    // General settings
    //-------------------
    configs.putString("bind", "127.0.0.1");   // bind only on localhost
    configs.putString("protected-mode", "yes");
    configs.putString("port", port);

    // WARNING: The TCP backlog setting of 511 cannot be enforced because /proc/sys/net/core/somaxconn is set to the lower value of 128.
    // instead of 511 (https://github.com/docker-library/redis/issues/35#issuecomment-339973076)
    configs.putString("tcp-backlog", "128");

    configs.putString("tcp-keepalive", "300");
    configs.putString("timeout", "0");
    configs.putString("daemonize", "no");
    configs.putString("supervised", "no");
    configs.putString("pidfile", port + ".pid");
    configs.putString("loglevel", "notice");
    configs.putString("logfile", "");
//    configs.putString("syslog-enabled", "no");
//    configs.putString("syslog-ident", "redis");
//    configs.putString("syslog-facility", "local0");
    configs.putString("databases", "16");
    configs.putString("always-show-logo", "yes");

    //-------------------
    // Snapshotting
    //-------------------
    ArrayList<String> save = new ArrayList<String>();
    save.add("900 1");
    save.add("300 10");
    save.add("60 10000");
    configs.putStringArrayList("save", save);

    configs.putString("stop-writes-on-bgsave-error", "yes");
    configs.putString("rdbcompression", "yes");
    configs.putString("rdbchecksum", "yes");
    configs.putString("dbfilename", port + ".rdb");
//    configs.putString("dir", "./");  <-- Don't set here. The value is specified later in this file.


    //-------------------
    // Replication
    //-------------------
    //configs.putString("replicaof", "no one");
    //configs.putString("masterauth", "");
    configs.putString("replica-serve-stale-data", "yes");
    configs.putString("replica-read-only", "yes");
    configs.putString("repl-diskless-sync", "no");
    configs.putString("repl-diskless-sync-delay", "5");
//    configs.putString("repl-ping-replica-period", "10");
//    configs.putString("repl-timeout", "60");
    configs.putString("repl-disable-tcp-nodelay", "no");
//    configs.putString("repl-backlog-size", "1mb");
//    configs.putString("repl-backlog-ttl", "3600");
    configs.putString("replica-priority", "100");
//    configs.putString("replica-announce-ip", "5.5.5.5");
//    configs.putString("replica-announce-port", "1234");

    //-------------------
    // Security
    //-------------------
    configs.putString("requirepass", packageName);

    //-------------------
    // Clients
    //-------------------
    configs.putString("maxclients", "100");

    //-------------------
    // Memory management
    //-------------------
    configs.putString("maxmemory", "10mb");
//    configs.putString("maxmemory-policy", "noeviction");
//    configs.putString("maxmemory-samples", "5");
//    configs.putString("replica-ignore-maxmemory", "yes");

    //-------------------
    // Lazy freeing
    //-------------------
    configs.putString("lazyfree-lazy-eviction", "no");
    configs.putString("lazyfree-lazy-expire", "no");
    configs.putString("lazyfree-lazy-server-del", "no");
    configs.putString("replica-lazy-flush", "no");

    //-------------------
    // Append only mode
    //-------------------
    configs.putString("appendonly", "yes");  // <--- Should be "yes" by default
    configs.putString("appendfilename", port + ".aof");
    configs.putString("appendfsync", "everysec");
    configs.putString("no-appendfsync-on-rewrite", "no");
    configs.putString("auto-aof-rewrite-percentage", "100");
    configs.putString("auto-aof-rewrite-min-size", "5mb");
    configs.putString("aof-load-truncated", "yes");
    configs.putString("aof-use-rdb-preamble", "yes");

    //-------------------
    // Lua scripting
    //-------------------
    configs.putString("lua-time-limit", "5000");

    //-------------------
    // Redis cluster
    //-------------------
//    configs.putString("cluster-enabled", "no");
//    configs.putString("cluster-config-file", "nodes-" + hashCode + ".conf");
//    configs.putString("cluster-node-timeout", "15000");
//    configs.putString("cluster-replica-validity-factor", "10");
//    configs.putString("cluster-migration-barrier", "1");
//    configs.putString("cluster-require-full-coverage", "yes");

    //-----------------------------
    // Cluster docker/nat support
    //-----------------------------
//    configs.putString("cluster-announce-ip", "10.1.1.5");
//    configs.putString("cluster-announce-port", hashCode + "");
//    configs.putString("cluster-announce-bus-port", (hashCode + 1) + "");

    //-----------------------------
    // Slow log
    //-----------------------------
    configs.putString("slowlog-log-slower-than", "10000");
    configs.putString("slowlog-max-len", "128");

    //-----------------------------
    // Latency monitor
    //-----------------------------
    configs.putString("latency-monitor-threshold", "0");

    //-----------------------------
    // Event notification
    //-----------------------------
    configs.putString("notify-keyspace-events", "");

    //-----------------------------
    // Advanced config
    //-----------------------------
    configs.putString("hash-max-ziplist-entries", "512");
    configs.putString("hash-max-ziplist-value", "64");
    configs.putString("list-max-ziplist-size", "-2");
    configs.putString("list-compress-depth", "0");
    configs.putString("set-max-intset-entries", "512");
    configs.putString("zset-max-ziplist-entries", "128");
    configs.putString("zset-max-ziplist-value", "64");
    configs.putString("hll-sparse-max-bytes", "3000");
    configs.putString("stream-node-max-bytes", "4096");
    configs.putString("stream-node-max-entries", "100");
    configs.putString("activerehashing", "yes");
    configs.putString("activerehashing", "yes");

    ArrayList<String> clientOutputBufferLimits = new ArrayList<String>();
    clientOutputBufferLimits.add("normal 0 0 0");
    clientOutputBufferLimits.add("replica 256mb 64mb 60");
    clientOutputBufferLimits.add("pubsub 32mb 8mb 60");
    configs.putStringArrayList("client-output-buffer-limit", clientOutputBufferLimits);
//    configs.putString("client-query-buffer-limit", "1gb");
//    configs.putString("proto-max-bulk-len", "512mb");

    configs.putString("hz", "10");
    configs.putString("dynamic-hz", "yes");
    configs.putString("aof-rewrite-incremental-fsync", "yes");
//    configs.putString("lfu-log-factor", "10");
//    configs.putString("lfu-decay-time", "1");

    //-----------------------------
    // Active defragmentation
    //-----------------------------
//    configs.putString("activedefrag", "yes");
//    configs.putString("active-defrag-ignore-bytes", "100mb");
//    configs.putString("active-defrag-threshold-lower", "10");
//    configs.putString("active-defrag-threshold-upper", "100");
//    configs.putString("active-defrag-cycle-min", "5");
//    configs.putString("active-defrag-cycle-max", "75");
//    configs.putString("active-defrag-max-scan-fields", "1000");


    StringBuilder stringBuilder = new StringBuilder();
    StringBuilder stringBuilderDebug = new StringBuilder();
    int i = 2;
    String value;
    Object valueObj;
    ArrayList<String> values;
    if (options != null) {
      configs.putAll(options);
    }


    //-------------------
    // working directory
    //-------------------
    if (configs.containsKey("dir")) {
      File dir = new File(configs.getString("dir") + "");
      if (!dir.exists()) {
        if (!dir.mkdirs()) {
          configs.remove("dir");
        }
      }
    }
    if (!configs.containsKey("dir")) {
      File dir = new File(context.getCacheDir().getAbsolutePath() + "/redis/");
      configs.putString("dir", dir.getAbsolutePath());
      if (!dir.exists()) {
        if (!dir.mkdirs()) {
          configs.remove("dir");
        }
      }
    }


    for (String key : configs.keySet()) {
      valueObj = configs.get(key);
      if (valueObj instanceof ArrayList) {
        values = (ArrayList<String>)valueObj;
        for (String value2 : values) {
          if (!value2.isEmpty()) {
            stringBuilderDebug.append(String.format(Locale.US, "%02d: %s %s\n", i++, key, value2));
            stringBuilder.append(String.format(Locale.US, "%s %s\n", key, value2));
          }
        }
      } else {
        value = valueObj + "";
        if (!value.isEmpty()) {
          stringBuilderDebug.append(String.format(Locale.US, "%02d: %s %s\n", i++, key, value));
          stringBuilder.append(String.format(Locale.US, "%s %s\n", key, value));
        }
      }
    }
    if ("debug".equals(configs.getString("loglevel")) ||
        "verbose".equals(configs.getString("loglevel")) ) {
      Log.d(TAG, stringBuilderDebug.toString());
    }

    String redisConfig = stringBuilder.toString();
    Log.i(TAG, "--------------------------------");
    Log.i(TAG, "Redis start on port " + configs.getString("port"));
    Log.i(TAG, "--------------------------------");

    int rc = native_redisStart(redisConfig);

    if (rc == 0) {
      Log.i(TAG, "--------------------------------");
      Log.i(TAG, "Redis exit safely");
      Log.i(TAG, "--------------------------------");
    } else {
      Log.e(TAG, "--------------------------------");
      Log.e(TAG, "Redis exit (status code: " + rc);
      Log.e(TAG, "--------------------------------");
    }
  }
}
