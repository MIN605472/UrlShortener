package liquidmountain.repository;

import liquidmountain.domain.ShortURL;

import java.util.List;



/**
 * Interface of ShortURLRepository
 */
public interface ShortURLRepository {

	/**
	 * Find one ShortUrl by @param id
	 * @param id: String
	 * @return ShortUrl
	 */
	ShortURL findByKey(String id);

	/**
	 * Find one ShortUrl by @target
	 * @param target: String
	 * @return
	 */
	List<ShortURL> findByTarget(String target);

	/**
	 * Save one ShortUrl in bd
	 * @param su: ShortURL
	 * @return the shortUrl saved
	 */
	ShortURL save(ShortURL su);

	/**
	 * Mark a url has @safeness
	 * @param urlSafe: ShortURL
	 * @param safeness: boolean true = safe/false = unsafe
	 * @return ShortUrl marked
	 */
	ShortURL mark(ShortURL urlSafe, boolean safeness);

	/**
	 * Update a ShortURL with the valor @su
	 * @param su: ShortURL
	 */
	void update(ShortURL su);

	/**
	 * Delete one ShortUrl with id like @param id
	 * @param id: String
	 */
	void delete(String id);

	/**
	 * @return numbers of ShortULR saved on bd
	 */
	Long count();

	/**
	 * List the saved click's.
	 * @param limit: Long limite de ShortUrl que se obtienen
	 * @param offset: Long
	 * @return
	 */
	List<ShortURL> list(Long limit, Long offset);

	/**
	 * @return List<ShortURL> all the ShortUrl's saved on bd.
	 */
	List<ShortURL> listAll();

}
