module.exports = {
  "/api/*": {
    target: "http://localhost:8888",
    changeOrigin: true,
    secure: false,
    logLevel: "debug",
  },
};
