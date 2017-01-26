package com.github.davidmoten.rx2.internal.flowable;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.util.Arrays;
import java.util.List;

import org.junit.Test;

import com.github.davidmoten.rx2.FlowableTransformers;

import io.reactivex.Flowable;

public class FlowableWindowMinMaxTest {
	@Test
	public void testEmpty() {
		boolean empty = Flowable.<Integer>empty().compose(FlowableTransformers.<Integer>windowMin(5)).isEmpty()
		        .blockingGet();
		assertTrue(empty);
	}

	@Test
	public void testIncreasing() {
		List<Integer> list = Flowable.just(1, 2, 3, 4).compose(FlowableTransformers.<Integer>windowMin(2)).toList()
		        .blockingGet();
		assertEquals(Arrays.asList(1, 2, 3), list);
	}

	@Test
	public void testDecreasing() {
		List<Integer> list = Flowable.just(4, 3, 2, 1).compose(FlowableTransformers.<Integer>windowMin(2)).toList()
		        .blockingGet();
		assertEquals(Arrays.asList(3, 2, 1), list);
	}

	@Test
	public void testWindowSizeBiggerThanAvailableProducesEmptyList() {
		List<Integer> list = Flowable.just(4, 3, 2, 1).compose(FlowableTransformers.<Integer>windowMin(10)).toList()
		        .blockingGet();
		assertTrue(list.isEmpty());
	}

	@Test
	public void testWindowMax() {
		List<Integer> list = Flowable.just(4, 3, 2, 1).compose(FlowableTransformers.<Integer>windowMax(2)).toList()
		        .blockingGet();
		assertEquals(Arrays.asList(4, 3, 2), list);
	}

	@Test(expected = IllegalArgumentException.class)
	public void testWindowSizeNegativeThrowsIAE() {
		Flowable.just(1).compose(FlowableTransformers.<Integer>windowMax(-2));
	}

	@Test(expected = IllegalArgumentException.class)
	public void testWindowSizeZeroThrowsIAE() {
		Flowable.just(1).compose(FlowableTransformers.<Integer>windowMax(0));
	}

	@Test
	public void testErrorPropagated() {
		RuntimeException r = new RuntimeException();
		Flowable.<Integer>error(r) //
		        .compose(FlowableTransformers.<Integer>windowMax(2)) //
		        .test() //
		        .assertError(r);
	}

}