DROP TABLE IF EXISTS photo;
DROP TABLE IF EXISTS photo_board;
DROP FUNCTION IF EXISTS random_versioned_uuid();
DROP FUNCTION IF EXISTS inc_versioned_uuid(versioned_uuid);
DROP TYPE IF EXISTS versioned_uuid;

CREATE TYPE versioned_uuid AS (
       uuid uuid,
       version integer
);

CREATE OR REPLACE FUNCTION random_versioned_uuid() RETURNS versioned_uuid AS $$
BEGIN
       RETURN (gen_random_uuid(), 1);
END
$$ LANGUAGE plpgsql; 

CREATE OR REPLACE FUNCTION inc_versioned_uuid(vu versioned_uuid) RETURNS versioned_uuid AS $$
BEGIN
       RETURN (vu.uuid, vu.version+1);
END
$$ LANGUAGE plpgsql; 

CREATE TABLE photo_board (
       id uuid DEFAULT gen_random_uuid(),
       version integer DEFAULT 1,
       name text,
       PRIMARY KEY (id,version),
       UNIQUE (id)
);

CREATE TABLE photo (
       id uuid DEFAULT gen_random_uuid() PRIMARY KEY,
       text text,
       url text,
       photo_board uuid REFERENCES photo_board (id)
);

INSERT INTO photo_board (name) VALUES ('foo');

WITH upd_info AS (
     SELECT id FROM photo_board WHERE name='foo' ORDER BY name LIMIT 1
) INSERT INTO photo (text,url,photo_board) SELECT 'srv2.elangley.org/~edwlan/systemd-depends.svg',upd_info.id FROM upd_info;
