function get(env, stringify = false) {
  const IS_ALPHA_CERT = env === "alpha-cert";
  const IS_PROD = env === "prod";

  let envs = {};
  if (IS_PROD) envs = require("./prod.env");

  envs.APP_ENV = env;
  envs.APP_IS_PROD = IS_PROD;

  if (!stringify) return envs;
  return Object.keys(envs).reduce((out, k) => {
    out[k] = JSON.stringify(envs[k]);
    return out;
  }, {});
}

module.exports = { get };
