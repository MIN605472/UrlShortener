package liquidmountain.repository;

import liquidmountain.domain.Click;

import java.util.List;


/**
 * Interface of ClickRepository
 */
public interface ClickRepository {

	/**
	 * Find a Click by @param hash
	 * @param hash
	 * @return List of click's
	 */
	List<Click> findByHash(String hash);

	/**
	 * Total of click's obtain by @param hash
	 * @param hash
	 * @return Long with count of clicks
	 */
	Long clicksByHash(String hash);

	/**
	 * Save a @param cl in bd
	 * @param cl
	 * @return
	 */
	Click save(Click cl);

	/**
	 * Update click valor with @param cl
	 * @param cl
	 */
	void update(Click cl);

	/**
	 * Delete click valor with @param cl
	 * @param id
	 */
	void delete(Long id);

	/**
	 * Delete all clicks
	 */
	void deleteAll();

	/**
	 * @return counts' number in bd.
	 */
	Long count();

	/**
	 * List the click on bd
	 * @param limit
	 * @param offset
	 * @return
	 */
	List<Click> list(Long limit, Long offset);
}
