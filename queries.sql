-- :name insert-new-photo-board :! name
-- :doc add a new photo board
insert into photo_board (name)
values (:name)

-- :name get-new-photo-board :! id
SELECT * FROM photo_board
WHERE (id.uuid) = id
