import request from '@/utils/request'

/**
 * 上传文件
 */
export const uploadFile = (file) => {
  const formData = new FormData()
  formData.append('file', file)
  
  return request({
    url: '/file/upload',
    method: 'post',
    data: formData,
    headers: {
      'Content-Type': 'multipart/form-data'
    }
  })
}

/**
 * 删除文件
 */
export const deleteFile = (url) => {
  return request({
    url: '/file/delete',
    method: 'delete',
    params: { url }
  })
}
