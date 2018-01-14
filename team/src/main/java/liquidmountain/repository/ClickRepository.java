package liquidmountain.repository;

import liquidmountain.domain.Click;

import java.util.List;


/**
 * Interface of ClickRepository
 */
public interface ClickRepository {

	/**
	 * Find a Click by @param hash
	 * @param String hash identificador del click
	 * @return List<Click> of click's que coinciden con el hash
	 */
	List<Click> findByHash(String hash);

	/**
	 * Total of click's obtain by @param hash
	 * @param String hash
	 * @return Long with count of clicks
	 */
	Long clicksByHash(String hash);

	/**
	 * Save a @param cl in bd
	 * @param Click click que se salva
	 * @return Click
	 */
	Click save(Click cl);

	/**
	 * Update click valor with @param cl
	 * @param Click cl que se actualizara
	 */
	void update(Click cl);

	/**
	 * Delete click valor with @param cl
	 * @param Long id, identificador del click que se eleminira
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
	 * @param Long limit, limite de clicks que se optienen
	 * @param Long offset
	 * @return
	 */
	List<Click> list(Long limit, Long offset);
}
