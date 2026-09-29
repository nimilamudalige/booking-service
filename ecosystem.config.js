module.exports = {
  apps: [
    {
      name: "booking-service",
      script: "java",
      args: "-jar booking-service.jar",
      cwd: "/opt/pulsefit/booking-service",
      env: {
        SERVER_PORT: "8083",
        CONFIG_SERVER_URL: "http://localhost:8888",
        EUREKA_SERVER_URL: "http://localhost:8761/eureka",
        // Point this at either a self-hosted MongoDB VM or a MongoDB Atlas cluster
        MONGODB_URI: "mongodb://<MONGO_HOST>:27017/pulsefit_booking_db",
        GCP_PROJECT_ID: "<YOUR_GCP_PROJECT_ID>", // used by the Firestore client
        FIRESTORE_ENABLED: "true"
      },
      autorestart: true,
      max_restarts: 10,
      min_uptime: "10s",
      restart_delay: 3000,
      out_file: "/var/log/pm2/booking-service-out.log",
      error_file: "/var/log/pm2/booking-service-error.log",
      log_date_format: "YYYY-MM-DD HH:mm:ss"
    }
  ]
};
