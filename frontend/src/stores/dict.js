import { defineStore } from 'pinia'
import { ref } from 'vue'
import { getDictDataByType } from '@/api/dict'

export const useDictStore = defineStore('dict', () => {
  const dictCache = ref({})

  /**
   * 获取字典数据（带缓存）
   */
  const getDictData = async (dictType) => {
    // 如果缓存中有数据，直接返回
    if (dictCache.value[dictType]) {
      return dictCache.value[dictType]
    }

    // 从服务器获取数据
    try {
      const res = await getDictDataByType(dictType)
      dictCache.value[dictType] = res.data
      return res.data
    } catch (error) {
      console.error('获取字典数据失败:', error)
      return []
    }
  }

  /**
   * 根据字典值获取字典标签
   */
  const getDictLabel = (dictType, dictValue) => {
    const dictData = dictCache.value[dictType]
    if (!dictData) return dictValue
    
    const item = dictData.find(d => d.dictValue === dictValue)
    return item ? item.dictLabel : dictValue
  }

  /**
   * 清空字典缓存
   */
  const clearDictCache = (dictType) => {
    if (dictType) {
      delete dictCache.value[dictType]
    } else {
      dictCache.value = {}
    }
  }

  return {
    dictCache,
    getDictData,
    getDictLabel,
    clearDictCache
  }
})
