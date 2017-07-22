CREATE TABLE IF NOT EXISTS bookmarks (
  user VARCHAR(64) NOT NULL,
  name VARCHAR(64) NOT NULL,
  short_url VARCHAR(32) NOT NULL,
  full_url VARCHAR(255) NOT NULL,
  created DATETIME DEFAULT CURRENT_TIMESTAMP NOT NULL,
  updated DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP NOT NULL,
  PRIMARY KEY (user, name),
  INDEX short_url_ix (short_url),
  INDEX user_name_ix (user, name)
);

INSERT INTO bookmarks (user, name, short_url, full_url) VALUES ('def', 'ynet', 'qw.123', 'http://www.ynet.co.il');
INSERT INTO bookmarks (user, name, short_url, full_url) VALUES ('def', 'walla', 'qw.456', 'http://www.walla.co.il');

