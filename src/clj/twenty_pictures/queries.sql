-- :name insert-new-photo-board :! name
-- :doc add a new photo board
INSERT INTO photo_board (name)
     VALUES (:name)

-- :name get-new-photo-board :! id version
SELECT *
  FROM photo_board
 WHERE id = :id
   AND (photo_board.id).version = :version

-- :name get-all-photo-boards
SELECT id::text,version,name
  FROM photo_board

-- :name get-photos-in-board-by-uuid :? id
    SELECT photo_board.name,photo.text,photo.url
      FROM photo_board
RIGHT JOIN photo
        ON photo_board.id = uuid(:id)
       AND photo.photo_board = photo_board.id
