package edu.emory.cs.sort.distribution;
import java.util.ArrayList;
import java.util.function.Function;
public class RadixSortQuiz extends RadixSort {
    @Override
    public void sort(Integer[] array, int beginIndex, int endIndex) {
        int maxDigits = getMaxBit(array, beginIndex, endIndex);
        order(array,beginIndex,endIndex,maxDigits);
    }
    public ArrayList getRange(Integer[] array, int beginIndex, int endIndex,int digit) {
        int div=(int)Math.pow(10,digit-1);//digit-1才能从MSD开始
       Function<Integer, Integer> bucketIndex = key -> (key/div)%10;

        for (int i = beginIndex; i < endIndex; i++){
            buckets.get(bucketIndex.apply(array[i])).add(array[i]);} //放到对应的bucket里
            ArrayList<Integer> b = new ArrayList<Integer>();

        for (int j = 0; j <= buckets.size() - 1; j++) {
            if (!buckets.get(j).isEmpty()) {
                b.add(beginIndex);
                while (!buckets.get(j).isEmpty()) {
                    array[beginIndex++] = buckets.get(j).remove();
                }
            }
        }
        b.add(endIndex);
        return b;  // the array ready to be sorted is returned

    }
    public void order(Integer[] array, int start, int end, int digit) {
        if(digit==0){
            return;
        }
        else if (digit<2) {// if there is only 1 digit place to be sorted, sort it directly
            sort(array,start,end,key->key%10);
        }
        else {
            ArrayList<Integer> range = getRange(array,start, end,digit);//把已经分好range的list assign到range
            for (int k = 0;k<range.size()-1;k++) {
                if (range.get(k+1)-range.get(k)>1) {
                    order(array,range.get(k),range.get(k+1),digit-1);
                }
            }
        }
    }
}
