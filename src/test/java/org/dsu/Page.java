/**
 * 
 */
package org.dsu;

import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;

/** Implements {@link Pagable} interface.
 *  It is used in the test enviroment.
 *
 * @author nescafe
 */
public final class Page implements Pageable {
	
	private final int pageNumber;
	private final int pageSize;
	private final Sort sort;
	
	public Page(int pageNumber, int pageSize, Sort sort) {
		this.pageNumber = pageNumber;
		this.pageSize = pageSize;
		this.sort = sort;
	}

	@Override
	public int getPageNumber() {
		return pageNumber;
	}

	@Override
	public int getPageSize() {
		return pageSize;
	}

	@Override
	public int getOffset() {
		return 0;
	}

	@Override
	public Sort getSort() {
		return sort;
	}

	@Override
	public Pageable next() {
		return null;
	}

	@Override
	public Pageable previousOrFirst() {
		return null;
	}

	@Override
	public Pageable first() {
		return null;
	}

	@Override
	public boolean hasPrevious() {
		return false;
	}

}
