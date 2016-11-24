;; This file is part of Twenty Pictures.
;;
;; Twenty Pictures is free software: you can redistribute it and/or
;; modify it under the terms of the GNU Affero General Public License
;; as published by the Free Software Foundation, either version 3 of
;; the License, or (at your option) any later version.
;;
;; Twenty Pictures is distributed in the hope that it will be useful,
;; but WITHOUT ANY WARRANTY; without even the implied warranty of
;; MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
;; Affero General Public License for more details.
;;
;; You should have received a copy of the GNU Affero General Public
;; License along with Twenty Pictures.  If not, see
;; <http://www.gnu.org/licenses/>.


(ns twenty-pictures.let-promise)

(defn unwind-bindings [bindings body]
  (let [the-binding (first bindings)
        remains (rest bindings)]
    `(.then ~(second the-binding)
            (fn [~(first the-binding)]
              ~@(if (empty? remains)
                  `(.resolve js/Promise
                             (do ~@body))
                  `(~(unwind-bindings remains body)))))))

(defmacro let-promise [[& bindings] & body]
  (unwind-bindings bindings body))
                  
