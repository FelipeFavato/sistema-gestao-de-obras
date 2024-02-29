import { toast} from 'vue3-toastify';

export function deleteSuccessToast (texto) {
  toast.success(texto, {
    autoClose: 3000
  });
}