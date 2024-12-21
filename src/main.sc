import logging
from telegram import Update
from telegram.ext import Updater, CommandHandler, CallbackContext

# Установите базу данных или список пользователей
subscribers = []

def start(update: Update, context: CallbackContext) -> None:
    update.message.reply_text('Привет! Отправьте /subscribe, чтобы подписаться на рассылку.')

def subscribe(update: Update, context: CallbackContext) -> None:
    user_id = update.message.from_user.id
    if user_id not in subscribers:
        subscribers.append(user_id)
        update.message.reply_text('Вы подписались на рассылку!')
    else:
        update.message.reply_text('Вы уже подписаны!')

def broadcast(update: Update, context: CallbackContext) -> None:
    message = ' '.join(context.args)
    for user_id in subscribers:
        context.bot.send_message(chat_id=user_id, text=message)
    update.message.reply_text('Сообщение отправлено всем подписчикам!')

def main():
    logging.basicConfig(format='%(asctime)s - %(name)s - %(levelname)s - %(message)s', level=logging.INFO)
    
    updater = Updater("YOUR_TELEGRAM_BOT_TOKEN")
    
    updater.dispatcher.add_handler(CommandHandler("start", start))
    updater.dispatcher.add_handler(CommandHandler("subscribe", subscribe))
    updater.dispatcher.add_handler(CommandHandler("broadcast", broadcast))

    updater.start_polling()
    updater.idle()

if __name__ == '__main__':
    main()