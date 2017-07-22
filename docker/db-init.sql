CREATE TABLE IF NOT EXISTS urls (
  user VARCHAR(64),
  name VARCHAR(64),
  full_url VARCHAR(255),
  short_url VARCHAR(32),
  created DATETIME DEFAULT CURRENT_TIMESTAMP,
  updated DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  PRIMARY KEY (user, name),
  INDEX urls_short_url_ix (short_url),
  INDEX urls_user_name_ix (user, name)
);

